# Kotlin 协程异常处理速查表

## 核心规则

### launch vs async

| | `launch` | `async` |
|---|---|---|
| 返回类型 | `Job` | `Deferred<T>` |
| 异常抛出时机 | **立即**，沿 Job 层级向上传播 | **延迟**，调用 `await()` 时才抛出 |
| 异常是否能取消父协程 | ✅ 是 | ✅ 是（异常发生时就取消，不等 await） |
| `CoroutineExceptionHandler` | ✅ 有效 | ❌ 对 async 无效 |

### 四种作用域的行为

| 作用域 | 子异常→取消兄弟 | 子异常→取消父级 | 挂起等待子协程？ |
|--------|:-----------:|:-----------:|:------------:|
| `coroutineScope` | 💀 取消 | 💀 re-throw | ✅ |
| `supervisorScope` | ✅ 不影响 | ✅ 不影响 | ✅ |
| `runBlocking` | 💀 取消 | 💀 re-throw到外部 | ✅ (阻塞线程) |
| `GlobalScope` | ✅ 不影响 | ✅ 无父级 | ❌ 不管 |

---

## 核心原理

### try-catch 的本质局限

```
try-catch 只能捕获"当前调用栈上同步抛出的异常"
```

- `launch` 开启新协程 → 异常在**另一个调用栈**中
- `coroutineScope` 挂起等待 → 把子协程异常**"接回来"**到当前调用栈 → re-throw
- `supervisorScope` 挂起等待 → 但子协程异常**不向上传播** → 不会 re-throw

### async 的陷阱

```
async 的异常在"发生时"就通过 Job 层级传播了
不是在"await()"时才传播
```

```
时间线:
  async { throw X }  ← 异常发生，Job 被标记取消，父协程被取消
  ...                ← 兄弟协程在这期间被取消
  await()            ← 你在这里 try-catch，只捕获了异常"值"
                     ← 但 Job 状态已经不可逆地被破坏了
  delay(...)         ← 💥 挂起点检查到 Job 已取消 → CancellationException
```

---

## launch 异常处理矩阵

图例: 🟢 = 接住了 + 兄弟活着 | 🟡 = 接住了但兄弟已死 | 🔴 = 没接住

| # | 作用域 | 处理方式 | 能捕获？ | 兄弟？ | 评级 |
|---|--------|---------|:------:|:-----:|:----:|
| 1 | `coroutineScope` | `try { coroutineScope { launch } } catch` | ✅ | 💀 | 🟡 |
| 2 | `coroutineScope` | `launch { try { ... } catch }` | ✅ 同步 | 💀 | 🟡 |
| 3 | `coroutineScope` | `try { launch } catch` | ❌ | 💀 | 🔴 |
| 4 | `supervisorScope` | `launch(CEH) { ... }` | ✅ | ✅ | 🟢 |
| 5 | `supervisorScope` | `launch { try { ... } catch }` | ✅ | ✅ | 🟢 |
| 6 | `supervisorScope` | `try { launch } catch` | ❌ | ✅ | 🔴 |
| 7 | `supervisorScope` | `launch { try { coroutineScope{ launch } } catch }` | ✅ | ✅ | 🟢 |
| 8 | `runBlocking` | `launch(CEH) { ... }` | ✅ | 💀 | 🟡 |
| 9 | `runBlocking` | `launch { try { ... } catch }` | ✅ | 💀 | 🟡 |
| 10 | `runBlocking` | `try { runBlocking { launch } } catch` | ✅ | 💀 | 🟡 |
| 11 | `GlobalScope` | `launch(CEH) { ... }` | ✅ | ✅ | 🟢 |

---

## async 异常处理矩阵

| # | 作用域 | 处理方式 | 能捕获？ | 兄弟？ | 评级 |
|---|--------|---------|:------:|:-----:|:----:|
| 1 | `coroutineScope` | `try { coroutineScope { async.await() } } catch` | ✅ | 💀 | 🟡 |
| 2 | `coroutineScope` | `try { async.await() } catch` | ✅ 值 | 💀 | 🟡 |
| 3 | `coroutineScope` | `async { try { ... } catch }` | ✅ | 💀 | 🟡 |
| 4 | `supervisorScope` | `try { async.await() } catch` | ✅ | ✅ | 🟢 |
| 5 | `supervisorScope` | `async { try { ... } catch }` | ✅ | ✅ | 🟢 |
| 6 | `supervisorScope` | `async(CEH) { ... }` | ❌ | ✅ | 🔴 |
| 7 | `runBlocking` | `try { async.await() } catch` | ✅ 值 | 💀 | 🟡 |
| 8 | `runBlocking` | `async { try { ... } catch }` | ✅ | 💀 | 🟡 |

---

## 速查总表

```
                     launch                      async
                 内try  外try  CEH          内try  await外try  CEH
─────────────────────────────────────────────────────────────────
coroutineScope    🟡     🟢*   🟡           🟡     🟡         ❌
supervisorScope   🟢     ❌    🟢           🟢     🟢         ❌
runBlocking       🟡     🟡*   🟡           🟡     🟡         ❌
GlobalScope       🟢     ❌    🟢           🟢     🟢         ❌

* 需要包在 scope 外部（如 try { coroutineScope { ... } } catch）
  但兄弟协程已被取消

🟢 = 异常被处理 + 兄弟协程存活
🟡 = 异常被处理 + 兄弟协程已被取消
❌ = 异常未被捕获，或该方法无效
```

---

## 推荐写法

```
场景                                          推荐写法
──────────────────────────────────────────────────────────────────
"一个挂了全停，我要知道是谁挂了"              try { coroutineScope { ... } } catch
"一个挂了别人继续，我要接住异常"              supervisorScope { try { ... } catch }
"一个挂了别人继续，我不管异常"                supervisorScope { launch(CEH) { ... } }
"我就想发射不管"                              GlobalScope.launch(CEH) { ... }
"async 拿结果，异常不影响别人"                supervisorScope { try { a.await() } catch }
```

---

## 关键结论

1. **try-catch 只能捕获同步调用栈上的异常**，不能直接捕获异步协程的异常
2. **coroutineScope 把异步异常"接回"同步调用栈** → try-catch 能捕获 → 但兄弟协程已被取消
3. **supervisorScope + try-catch 是唯一能做到"捕获异常 + 兄弟不受影响"的组合**
4. **async 的异常在发生时就传播了**，try-catch 包 await() 只能接住"值"，不能 undo 取消状态
5. **CEH 只对 launch 有效，对 async 无效**
