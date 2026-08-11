# Kotlin 协程异常处理速查表

## 核心规则

### launch vs async

| | 返回类型 | 异常抛出时机 | 异常取消父协程 | CEH |
|---|------|----------|:----------:|:---:|
| `launch` | `Job` | **立即**，沿 Job 层级向上传播 | ✅ 是 | ✅ 有效（取决于作用域） |
| `async` | `Deferred<T>` | **延迟**，`await()` 时才抛出 | ✅ 是（发生时就取消，不等 await） | ❌ 无效 |

---

## 四种作用域的对比

### 一表看懂

| 作用域 | 类型 | 等待子协程 | 子异常→取消兄弟 | 子异常→取消自己 | 子异常→父级 | 适用场景 |
|--------|------|:------:|:---------:|:---------:|:------:|------|
| `coroutineScope` | suspend | ✅ 挂起 | 💀 取消 | 💀 取消 | 💀 re-throw 根因 | "一个挂全停" |
| `supervisorScope` | suspend | ✅ 挂起 | ✅ 不影响 | ✅ 不影响 | ✅ 不影响 | "各管各的" |
| `runBlocking` | 普通函数 | ✅ 阻塞 | 💀 取消 | 💀 取消 | 💀 re-throw 根因 | 桥接阻塞/挂起 |
| `GlobalScope` | 单例对象 | ❌ 不管 | ✅ 不影响 | N/A 无父级 | N/A 无父级 | 不推荐 |

### 详解

**coroutineScope** — 结构化并发的执行单元
```
coroutineScope {
    launch { error() }  ← 异常立即取消所有兄弟，re-throw 到调用处
    launch { ... }      ← 被取消
}
```
- 就像一个"事务"：任何一个子任务失败，全部回滚
- re-throw 的是**根因异常**（顺着 cause 链回溯），不是 CancellationException
- 适合：一组必须全部成功的任务

**supervisorScope** — 独立监督的执行单元
```
supervisorScope {
    launch { error() }  ← 异常不影响兄弟，也不取消 supervisorScope 自己
    launch { ... }      ← 正常执行 ✅
}
```
- 子协程各自独立，一个挂了别人不受影响
- 但注意：supervisorScope 自己的 block 抛了异常，照样往外传（只拦 Job 层级，不拦调用栈）
- 适合：一组互不依赖的独立任务

**runBlocking** — 阻塞世界的入口
```
runBlocking {
    launch { error() }  ← 行为和 coroutineScope 一样，但阻塞当前线程
}
```
- 本质上是 `coroutineScope` + 阻塞线程
- 只应该用在 main() 或测试中，作为挂起世界和阻塞世界的桥梁
- **绝不要在协程内部调用 runBlocking**

**GlobalScope** — 无父无母的孤儿
```
GlobalScope.launch { error() }  ← 异常无处传播，必须有 CEH 处理
```
- 生命周期跟应用一样长，无法被结构化取消
- 异常必须靠 CEH 处理，否则直接传到线程的 uncaughtExceptionHandler
- **不推荐使用**，除非你很清楚自己在干什么

### 选作用域决策树

```
子任务之间是否相互依赖？
  ├─ 是，一个失败全停 → coroutineScope
  └─ 否，各管各的
      ├─ 需要等待所有完成 → supervisorScope
      └─ 发射后不管 → 别用 GlobalScope，重构为 supervisorScope + launch
```

---

## 核心原理

### 异常传播的两条路径（互不干扰）

```
异常发生 ──┬── 路径A: Job 层级 ────→ supervisorScope 能拦 ✅
           │    (子Job异常 → 取消父Job → 取消兄弟)
           │
           └── 路径B: 调用栈 ──────→ 只有 try-catch 能拦
                (block 体 throw → 沿 suspend 链向上)
```

- **supervisorScope 只管路径A**：子协程异常不取消父 Job
- **但拦不住路径B**：如果 supervisorScope 自己的 block 抛了异常（比如内部 coroutineScope 的 re-throw），照样往外传
- **两条路各管各的**，完整覆盖需要同时考虑两者

### try-catch 的本质局限

```
try-catch 只能捕获"当前调用栈上同步抛出的异常"
```

- `launch` 开启新协程 → 异常在**另一个调用栈**中 → try 包 launch 外面无效
- `coroutineScope` 挂起等待 → 把子协程异常**"接回来"**到当前调用栈 → re-throw → try 能捕获
- `supervisorScope` 挂起等待 → 但子协程异常**不向上传播** → try 包 supervisorScope 外面无效

### async 的陷阱

```
async 的异常在"发生时"就通过 Job 层级传播了
不是在"await()"时才传播
```

```
时间线:
  async { throw X }  ← 异常发生，Job 被标记取消，父协程已取消
  ...                ← 兄弟协程在这期间已经被取消
  await()            ← 你在这里 try-catch，只捕获了异常"值"
                     ← 但 Job 状态已经不可逆地被破坏了
  nextSuspend()      ← 💥 挂起点检查到 Job 已取消 → 抛异常
```

### coroutineScope / runBlocking re-throw 的是根因

```
async → RuntimeException ← 根因
  → coroutineScope Job 取消
    → delay() → CancellationException ← 表象
      → coroutineScope 回溯 cause 链 → 找到 RuntimeException
        → re-throw 的是 RuntimeException！不是 CancellationException！
```

所以 `catch (e: CancellationException)` 可能根本接不住——飞来的是根因异常。

### coroutineScope 作为"防火墙"

**关键概念**：给 async 的异常传播路径上**插一层 coroutineScope**，可以阻断 Job 层级传播。

```
成功 🟢 (async 在防火墙内):             失败 🟡 (async 在防火墙外):

launch {                                launch {
    try {                                   val f = async(💥) ← 父Job=launch!
        coroutineScope {  ← 防火墙           try {
            async(💥) ← 防火墙的子Job            coroutineScope {
        }   ↑ 异常卡在这一层                       f.await()
    } catch { ✅ }                            } catch { ✅ 只捕获了"值" }
    "正常结束"                              }
}                                        }
兄弟(外层) ← ✅ 不受影响                 兄弟(外层) ← ❌ Job已取消
```

**防火墙生效条件**：async 必须是 coroutineScope 的**直接或间接子 Job**。
如果 async 在 coroutineScope 外面定义，异常直接传播到外层，防火墙拦不住。

---

## 内部 try-catch vs 外部 try-catch

```
内部 try (try 在抛出异常的协程体内):
  launch { try { error() } catch { ... } }   ← 异常被 catch，不传播！🟢
  
外部 try (try 包在协程外面):
  try { launch { error() } } catch { ... }   ← launch 立即返回，try 已结束 🔴
```

**关键：异常在协程内部被 catch 了，就不会传播到 Job 层级。父协程不受影响，兄弟存活。**

---

## launch 异常处理矩阵

图例: 🟢 = 接住了 + 兄弟活着 | 🟡 = 接住了但兄弟已死 | 🔴 = 没接住

| # | 作用域 | 处理方式 | 能阻止传播？ | 兄弟？ | 评级 |
|---|--------|---------|:----------:|:-----:|:----:|
| 1 | `coroutineScope` | `try { coroutineScope { launch } } catch` | ✅ 外部接住 | 💀 已取消 | 🟡 |
| 2 | `coroutineScope` | `launch { try { ... } catch }` | ✅ 内部阻止 | ✅ | 🟢 |
| 3 | `coroutineScope` | `try { launch } catch` | ❌ | 💀 | 🔴 |
| 4 | `coroutineScope` | `launch { try { coroutineScope{ launch } } catch }` | ✅ 防火墙 | ✅ | 🟢 |
| 5 | `supervisorScope` | `launch(CEH) { ... }` | ✅ CEH消费 | ✅ | 🟢 |
| 6 | `supervisorScope` | `launch { try { ... } catch }` | ✅ 内部阻止 | ✅ | 🟢 |
| 7 | `supervisorScope` | `try { launch } catch` | ❌ | ✅ 但异常泄露 | 🔴 |
| 8 | `supervisorScope` | `launch { try { coroutineScope{ launch } } catch }` | ✅ | ✅ | 🟢 |
| 9 | `runBlocking` | `launch(CEH) { ... }` | ❌ CEH只打印 | 💀 | 🟡 |
| 10 | `runBlocking` | `launch { try { ... } catch }` | ✅ 内部阻止 | ✅ | 🟢 |
| 11 | `runBlocking` | `try { runBlocking { launch } } catch` | ✅ 外部接住 | 💀 内全灭 | 🟡 |
| 12 | `GlobalScope` | `launch(CEH) { ... }` | ✅ CEH消费 | ✅ | 🟢 |

**CEH 的关键区别**：
- `supervisorScope` 中：CEH 在 launch 上 → 异常被 CEH **消费**，不传播 ✅
- `runBlocking` / `coroutineScope` 中：CEH 在 launch 上 → CEH **只打印**，异常仍传播，scope 仍被取消 ❌

---

## async 异常处理矩阵

| # | 作用域 | 处理方式 | 能阻止传播？ | 兄弟？ | 评级 |
|---|--------|---------|:----------:|:-----:|:----:|
| 1 | `coroutineScope` | `try { coroutineScope { async.await() } } catch` | ✅ 外部接住 | 💀 | 🟡 |
| 2 | `coroutineScope` | `try { async.await() } catch` | ❌ 只捕获值 | 💀 Job已取消 | 🟡 |
| 3 | `coroutineScope` | `launch { try { coroutineScope{ async{...}.await() } } catch }` | ✅ 防火墙 | ✅ | 🟢 |
| 4 | `coroutineScope` | `launch { val f=async{...}; try { coroutineScope{ f.await() } } catch }` | ❌ async在墙外 | 💀 | 🟡 |
| 5 | `coroutineScope` | `async { try { ... } catch }` | ✅ 内部阻止 | ✅ | 🟢 |
| 6 | `supervisorScope` | `try { async.await() } catch` | ✅ | ✅ | 🟢 |
| 7 | `supervisorScope` | `async { try { ... } catch }` | ✅ 内部阻止 | ✅ | 🟢 |
| 8 | `supervisorScope` | `async(CEH) { ... }` | ❌ CEH对async无效 | ✅ | 🔴 |
| 9 | `runBlocking` | `try { async.await() } catch` | ❌ 只捕获值 | 💀 Job已取消 | 🟡 |
| 10 | `runBlocking` | `async { try { ... } catch }` | ✅ 内部阻止 | ✅ | 🟢 |

**#3 vs #4 的关键区别**：
```
#3 🟢 async 定义在 coroutineScope 内部 → async 是 coroutineScope 的子Job → 异常被防火墙接住
#4 🟡 async 定义在 coroutineScope 外部 → async 的父Job 是 launch → 异常绕过防火墙
```

---

## 速查总表

```
                     launch                      async
                 内try  外try  CEH          内try  await外try  CEH  防火墙
─────────────────────────────────────────────────────────────────────────
coroutineScope    🟢     🟡*   🟡¹          🟢     🟡         ❌    🟢/🟡²
supervisorScope   🟢     ❌    🟢           🟢     🟢         ❌    —
runBlocking       🟢     🟡*   🟡¹          🟢     🟡         ❌    —
GlobalScope       🟢     ❌    🟢           🟢     🟢         ❌    —

* 需要包在 scope 外部（如 try { coroutineScope { ... } } catch）
  兄弟协程已被取消
¹ CEH 只打印异常但不阻止传播，scope 仍被取消
² 🟢=async在墙内 / 🟡=async在墙外
```

---

## 推荐写法

```
场景                                          推荐写法
──────────────────────────────────────────────────────────────────
"一个挂了全停，我要知道是谁挂了"              try { coroutineScope { ... } } catch
"一个挂了别人继续，我要接住异常"              ① 协程内部 try-catch（最简单）
                                             ② supervisorScope + try-catch（隔离最好）
                                             ③ 加一层 coroutineScope 防火墙
"一个挂了别人继续，我不管异常"                supervisorScope { launch(CEH) { ... } }
"async 拿结果，异常不影响别人"               ① supervisorScope { try { a.await() } catch }
                                             ② 协程内部 try-catch
"async 用 coroutineScope 外兜底"             把 async 定义在 coroutineScope 内部 → 防火墙
```

---

## 关键结论

1. **异常传播有两条路径**：Job 层级（supervisorScope 能拦） + 调用栈（只有 try-catch 能拦），互不干扰
2. **coroutineScope 可以当防火墙**：async 定义在 coroutineScope **内部** → 异常被接住不传播 🟢；定义在**外部** → 防火墙失效 🟡
3. **内部 try-catch 能阻止异常传播**：异常在协程内部被 catch → 不进入 Job 层级 → 兄弟存活
4. **外部 try-catch 只能接住值**：coroutineScope/runBlocking 在异常发生时已被取消，不可逆
5. **coroutineScope/runBlocking re-throw 的是根因异常**，不是 CancellationException
6. **CEH 的行为取决于作用域**：supervisorScope 中能消费异常；coroutineScope/runBlocking 中只打印
7. **CEH 对 async 完全无效**，只能用 await + try-catch
8. **选作用域**：一个挂全停用 coroutineScope，各管各的用 supervisorScope，桥接用 runBlocking，别用 GlobalScope
