# Kotlin 协程异常处理速查表

## 核心规则

### launch vs async

| | `launch` | `async` |
|---|---|---|
| 返回类型 | `Job` | `Deferred<T>` |
| 异常抛出时机 | **立即**，沿 Job 层级向上传播 | **延迟**，调用 `await()` 时才抛出 |
| 异常是否能取消父协程 | ✅ 是 | ✅ 是（异常发生时就取消，不等 await） |
| `CoroutineExceptionHandler` | ✅ 有效（取决于作用域） | ❌ 对 async 无效 |

### 四种作用域的行为

| 作用域 | 子异常→取消兄弟 | 子异常→取消父级 | 挂起等待子协程？ |
|--------|:-----------:|:-----------:|:------------:|
| `coroutineScope` | 💀 取消 | 💀 re-throw **根因异常** | ✅ |
| `supervisorScope` | ✅ 不影响 | ✅ 不影响 | ✅ |
| `runBlocking` | 💀 取消 | 💀 re-throw **根因异常**到外部 | ✅ (阻塞线程) |
| `GlobalScope` | ✅ 不影响 | ✅ 无父级 | ❌ 不管 |

---

## 核心原理

### 异常传播的两条路径（互不干扰）

```
异常发生 ──┬── 路径A: Job 层级 ────→ supervisorScope 能拦 ✅
           │    (子Job异常 → 取消父Job)
           │
           └── 路径B: 调用栈 ──────→ 只有 try-catch 能拦
                (block 体 throw → 沿 suspend 链向上)
```

- **supervisorScope 只管路径A**：子协程异常不取消父 Job
- **但拦不住路径B**：如果 supervisorScope 自己的 block 抛了异常（比如内部的 coroutineScope re-throw），照样往外传
- **所以两条路各管各的**，完整处理需要 supervisorScope + try-catch 配合

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
  async { throw X }  ← 异常发生，Job 被标记取消，父协程被取消
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

---

## 内部 try-catch vs 外部 try-catch

**这是最容易被误解的地方：**

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
| 4 | `supervisorScope` | `launch(CEH) { ... }` | ✅ CEH消费 | ✅ | 🟢 |
| 5 | `supervisorScope` | `launch { try { ... } catch }` | ✅ 内部阻止 | ✅ | 🟢 |
| 6 | `supervisorScope` | `try { launch } catch` | ❌ | ✅ 但异常泄露到父协程 | 🔴 |
| 7 | `supervisorScope` | `launch { try { coroutineScope{ launch } } catch }` | ✅ | ✅ | 🟢 |
| 8 | `runBlocking` | `launch(CEH) { ... }` | ❌ CEH只打印 | 💀 RB被取消 | 🟡 |
| 9 | `runBlocking` | `launch { try { ... } catch }` | ✅ 内部阻止 | ✅ | 🟢 |
| 10 | `runBlocking` | `try { runBlocking { launch } } catch` | ✅ 外部接住 | 💀 RB内全灭 | 🟡 |
| 11 | `GlobalScope` | `launch(CEH) { ... }` | ✅ CEH消费 | ✅ | 🟢 |

**CEH 的关键区别**：
- `supervisorScope` 中：CEH 在 launch 上 → 异常被 CEH **消费**，不传播 ✅
- `runBlocking` / `coroutineScope` 中：CEH 在 launch 上 → CEH **只打印**，异常仍传播，scope 仍被取消 ❌

---

## async 异常处理矩阵

| # | 作用域 | 处理方式 | 能阻止传播？ | 兄弟？ | 评级 |
|---|--------|---------|:----------:|:-----:|:----:|
| 1 | `coroutineScope` | `try { coroutineScope { async.await() } } catch` | ✅ 外部接住 | 💀 | 🟡 |
| 2 | `coroutineScope` | `try { async.await() } catch` | ❌ 只捕获值 | 💀 Job已取消 | 🟡 |
| 3 | `coroutineScope` | `async { try { ... } catch }` | ✅ 内部阻止 | ✅ | 🟢 |
| 4 | `supervisorScope` | `try { async.await() } catch` | ✅ | ✅ | 🟢 |
| 5 | `supervisorScope` | `async { try { ... } catch }` | ✅ 内部阻止 | ✅ | 🟢 |
| 6 | `supervisorScope` | `async(CEH) { ... }` | ❌ CEH对async无效 | ✅ | 🔴 |
| 7 | `runBlocking` | `try { async.await() } catch` | ❌ 只捕获值 | 💀 Job已取消 | 🟡 |
| 8 | `runBlocking` | `async { try { ... } catch }` | ✅ 内部阻止 | ✅ | 🟢 |

---

## 速查总表

```
                     launch                      async
                 内try  外try  CEH          内try  await外try  CEH
─────────────────────────────────────────────────────────────────
coroutineScope    🟢     🟡*   🟡¹          🟢     🟡         ❌
supervisorScope   🟢     ❌    🟢           🟢     🟢         ❌
runBlocking       🟢     🟡*   🟡¹          🟢     🟡         ❌
GlobalScope       🟢     ❌    🟢           🟢     🟢         ❌

* 需要包在 scope 外部（如 try { coroutineScope { ... } } catch）
  兄弟协程已被取消
¹ CEH 只打印异常但不阻止传播，scope 仍被取消

🟢 = 异常被处理 + 兄弟协程存活  (10 种)
🟡 = 异常被处理 + 兄弟协程已被取消 (6 种)
🔴 = 异常未被捕获，或该方法无效   (3 种)
```

---

## 推荐写法

```
场景                                          推荐写法
──────────────────────────────────────────────────────────────────
"一个挂了全停，我要知道是谁挂了"              try { coroutineScope { ... } } catch
"一个挂了别人继续，我要接住异常"              ① supervisorScope { try { ... } catch }
                                             ② 在协程内部 try-catch（不依赖作用域）
"一个挂了别人继续，我不管异常"                supervisorScope { launch(CEH) { ... } }
"async 拿结果，异常不影响别人"                supervisorScope { try { a.await() } catch }
"async 拿结果，内部处理好异常"                async { try { ... } catch }; deferred.await()
```

---

## 关键结论

1. **异常传播有两条路径**：Job 层级（supervisorScope 能拦） + 调用栈（只有 try-catch 能拦），两条路互不干扰
2. **内部 try-catch 能阻止异常传播**：异常在协程内部被 catch → 不进入 Job 层级 → 父协程不受影响 → 兄弟存活
3. **外部 try-catch 只能接住值，不能复活已取消的 scope**：coroutineScope/runBlocking 在异常发生时已被取消
4. **coroutineScope/runBlocking re-throw 的是根因异常**，不是 CancellationException
5. **CEH 的行为取决于作用域**：supervisorScope 中能消费异常；coroutineScope/runBlocking 中只打印
6. **CEH 对 async 完全无效**，只能用 await + try-catch
7. **要想"捕获异常 + 兄弟存活"，两条路任选其一**：
   - 在抛出异常的协程**内部** try-catch（最简单）
   - supervisorScope + 外部 try-catch（隔离性最好）
