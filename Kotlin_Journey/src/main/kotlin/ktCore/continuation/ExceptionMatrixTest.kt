package ktCore.continuation

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds

/**
 * 协程异常处理矩阵 —— 19 种组合的完整测试
 *
 * 对照 CoroutineExceptionReference.md 速查表
 *
 * 图例:
 *   🟢 = 异常被处理 + 兄弟协程存活
 *   🟡 = 异常被处理 + 兄弟协程已被取消
 *   🔴 = 异常未被捕获，或该方法无效
 */

val CEH = CoroutineExceptionHandler { _, e ->
    println("   >>> CEH 捕获: ${e.message}")
}

// ============================================================
//                     launch × coroutineScope
// ============================================================

/** #1 🟡 coroutineScope: try 包在 coroutineScope 外部 */
suspend fun launchCoroutineScope_outerTry() {
    println("\n=== #1 🟡 launch + coroutineScope + try 包 coroutineScope 外部 ===")
    try {
        coroutineScope {
            launch { delay(50); println("   兄弟-1 启动"); delay(200); println("   兄弟-1 完成") }
            launch { delay(100); error("💥子异常") }
            launch { delay(150); println("   兄弟-2 完成") }  // 永远不会打印
        }
    } catch (e: Exception) {
        println("   ✅ catch 捕获: ${e.message}")
    }
    println("   → 结论: try-catch 接住了异常，但兄弟-2 已被取消，异常传递到兄弟-1\n")
}

/** #2 🟢 coroutineScope: try 包在 launch 内部 — 内部catch阻止传播 */
suspend fun launchCoroutineScope_innerTry() {
    println("\n=== #2 🟢 launch + coroutineScope + try 包 launch 内部(内部catch阻止传播) ===")
    coroutineScope {
        launch {
            try {
                delay(100); error("💥子异常")
            } catch (e: Exception) {
                println("   ✅ catch 捕获: ${e.message}")
            }
        }
        launch { delay(50); println("   兄弟启动"); delay(200); println("   兄弟完成") }
    }
    println("   → 结论: try 内部直接抛出异常，没有转移调用栈， 内部 try 接住了，兄弟完全完成 \n")
}

/** #3 🔴 coroutineScope: try 包在 launch 外部 */
suspend fun launchCoroutineScope_tryOutsideLaunch() {
    println("\n=== #3 🔴 launch + coroutineScope + try 包 launch 外面(调用栈转移) ===")
    try {
        coroutineScope {
            try {
                launch { delay(50); error("💥子异常") }  // try 在 launch 外面
            } catch (e: Exception) {
                println("   ⚠️ 这行永远不会打印")
            }
            launch {; println("   兄弟启动"); delay(200); println("   兄弟完成") }
        }
    } catch (e: Exception) {
        println("   ✅ 只有外层的 coroutineScope catch 能捕获: ${e.message}")
    }
    println("   → 结论: try 包 launch 外面， 调用栈转移，抓不到异常，launch 立即返回了。\n")
}

/** #4 🟢 coroutineScope: launch 内部嵌套 coroutineScope + try */
suspend fun launchCoroutineScope_innerCoroutineScope() {
    println("\n=== #4 🟢 launch + coroutineScope + 内部 coroutineScope + try ===")
    coroutineScope {
        launch {
            try {
                coroutineScope {
                    launch { delay(50); error("💥子异常") }
                }
            } catch (e: Exception) {
                println("   ✅ catch 捕获: ${e.message}")
            }
            println("   launch-1 正常结束 ✅")
        }
        launch { println("   兄弟启动"); delay(100); println("   兄弟完成 ✅") }
        delay(200)
        println("   外层 coroutineScope 正常结束 ✅")
    }
    println("   → 结论: 内部 try 接住 coroutineScope 的 re-throw → launch-1 正常完成 → 兄弟存活\n")
}


// ============================================================
//                     launch × supervisorScope
// ============================================================

/** #5 🟢 supervisorScope: launch(CEH) */
suspend fun launchSupervisorScope_CEH() {
    println("\n=== #5 🟢 launch + supervisorScope + CEH ===")
    supervisorScope {
        launch(CEH) { delay(50); error("💥子异常") }
        launch { delay(100); println("   兄弟完成 ✅") }
        launch { delay(200); println("   兄弟-2 完成 ✅") }
        delay(300)
    }
    println("   → 结论: CEH 捕获 + 兄弟全部存活，完美\n")
}

/** #6 🟢 supervisorScope: try 包在 launch 内部 */
suspend fun launchSupervisorScope_innerTry() {
    println("\n=== #6 🟢 launch + supervisorScope + try 包内部 ===")
    supervisorScope {
        launch {
            try {
                delay(50); error("💥子异常")
            } catch (e: Exception) {
                println("   ✅ catch 捕获: ${e.message}")
            }
        }
        launch {  println("   兄弟启动"); delay(100); println("   兄弟完成 ✅") }
        delay(200)
    }
    println("   → 结论: 内部 try 捕获 + 兄弟存活，完美\n")
}

/** #7 🔴 supervisorScope: try 包在 launch 外部 */
suspend fun launchSupervisorScope_tryOutsideLaunch() {
    println("\n=== #7 🔴 launch + supervisorScope + try 包 launch 外面 ===")
    supervisorScope {
        try {
            launch { delay(50); error("💥子异常") }
        } catch (e: Exception) {
            println("   ⚠️ 这行永远不会打印")
        }
        launch {  println("   兄弟启动"); delay(100); println("   兄弟完成 ✅ (但异常去哪了?)") }
        delay(200)
    }
    println("   → 结论: try 包 launch 外面抓不到! supervisorScope 不向上传播，异常被吞了\n")
}

/** #8 🟢 supervisorScope: launch 内部嵌套 coroutineScope + try */
suspend fun launchSupervisorScope_innerCoroutineScope() {
    println("\n=== #8 🟢 launch + supervisorScope + 内部 coroutineScope + try ===")
    supervisorScope {
        launch {
            try {
                coroutineScope {
                    launch { delay(50); error("💥子异常") }
                }
            } catch (e: Exception) {
                println("   ✅ catch 捕获: ${e.message}")
            }
        }
        launch { delay(100); println("   兄弟完成 ✅") }
        delay(200)
    }
    println("   → 结论: 内部 coroutineScope 把异常 re-throw → 外部 try 接住\n")
}


// ============================================================
//                     launch × runBlocking
// ============================================================

/** #9 🟡 runBlocking: launch(CEH)——CEH只打印不阻止传播 */
suspend fun launchRunBlocking_CEH() {
    println("\n=== #9 🟡 launch + runBlocking + CEH(CEH只打印不阻止传播) ===")
    try {
        runBlocking {
            launch(CEH) { delay(50); error("💥子异常") }
            launch { println("   兄弟尝试..."); delay(200); println("   兄弟完成? 不会打印") }
            launch { delay(350); println("   永远不会打印") }
            delay(400)
        }
    } catch (e: Exception) {
        println("   ⚠️ runBlocking 本身也被取消: ${e.message}")
    }
    println("   → 结论: CEH 只是打印了异常，但异常仍传播，runBlocking 被取消\n")
}

/** #10 🟢 runBlocking: try 包在 launch 内部 — 内部catch阻止传播 */
suspend fun launchRunBlocking_innerTry() {
    println("\n=== #10 🟢 launch + runBlocking + try 包内部(内部catch阻止传播) ===")
    runBlocking {
        launch {
            try {
                delay(50); error("💥子异常")
            } catch (e: Exception) {
                println("   ✅ catch 捕获: ${e.message}")
            }
        }
        launch { println("   兄弟尝试..."); delay(200); println("   兄弟完成 ✅") }
        delay(300)
        println("   后续代码正常 ✅")
    }
    println("   → 结论: 内部 try 阻止了异常传播，runBlocking 未被取消\n")
}

/** #11 🟡 runBlocking: try 包在 runBlocking 外部 */
fun launchRunBlocking_outerTry() {
    println("\n=== #11 🟡 launch + runBlocking + try 包外部 ===")
    try {
        runBlocking {
            launch { delay(50); error("💥子异常") }
            launch { println("   兄弟尝试..."); delay(200); println("   兄弟完成? 不会打印")  }
            delay(200)
        }
    } catch (e: Exception) {
        println("   ✅ catch 捕获: ${e.message}")
    }
    println("   → 结论: 接住了，但 runBlocking 内的一切都没了\n")
}


// ============================================================
//                     launch × GlobalScope
// ============================================================

/** #12 🟢 GlobalScope: launch(CEH) */
fun launchGlobalScope_CEH() {
    println("\n=== #12 🟢 launch + GlobalScope + CEH ===")
    val job1 = GlobalScope.launch(CEH) { delay(50); error("💥子异常") }
    val job2 = GlobalScope.launch { println("   兄弟尝试...");delay(100); println("   兄弟完成 ✅") }
    runBlocking {
        job1.join()
        job2.join()
        delay(100)
    }
    println("   → 结论: CEH 捕获 + 兄弟存活。但别用 GlobalScope，没法管理生命周期\n")
}


// ============================================================
//                     async × coroutineScope
// ============================================================

/** #1 🟡 coroutineScope: try 包在 coroutineScope 外部 + async */
suspend fun asyncCoroutineScope_outerTry() {
    println("\n=== #1 🟡 async + coroutineScope + try 包外部 ===")
    try {
        coroutineScope {
            val f = async { delay(50); error("💥子异常"); 1 }
            val n = async { println("   兄弟尝试..."); delay(200); println("   兄弟完成? 不会打印"); 1 }
            f.await()
            n.await()
        }
    } catch (e: Exception) {
        println("   ✅ catch 捕获: ${e.message}")
    }
    println("   → 结论: coroutineScope re-throw → 外部接住，但兄弟全灭\n")
}

/** #2 🟡 coroutineScope: try 包在 await() 外部 */
suspend fun asyncCoroutineScope_tryAwait() {
    println("\n=== #2 🟡 async + coroutineScope + try 包 await 外部 ===")
    try {
        coroutineScope {
            val failing = async { delay(50); error("💥子异常"); 1 }
            val normal  = async { println("   兄弟尝试..."); delay(100); println("   兄弟完成? 不会打印"); 1 }
            try {
                failing.await()
            } catch (e: Exception) {
                println("   ✅ catch 捕获值: ${e.message}")
            }
            // 走到这里时 coroutineScope 的 Job 已被取消
            // 挂起点会检查到 Job 已取消 → 抛异常
            normal.await()
            println("   这行不会打印")
        }
    } catch (e: CancellationException) {
        println("   💀 coroutineScope 因取消而结束(CancellationException)")
    } catch (e: Exception) {
        println("   💀 coroutineScope re-throw 了原始异常(${e::class.simpleName}: ${e.message})")
    }
    println("   → 结论: 捕获了异常值，但 coroutineScope Job 早已被取消\n")
}

/** #3 🟢 coroutineScope: async 外再套一层 try + coroutineScope 做防火墙 */
suspend fun asyncCoroutineScope_wrapCoroutineScope() {
    println("\n=== #3 🟢 async + coroutineScope + 外层 try+coroutineScope 防火墙 ===")
    coroutineScope {  // 外层 coroutineScope
        launch {       // 用 launch 包裹，隔离 Job 层级
            try {
                // 内层 coroutineScope 是 async 的直接父Job
                // async 异常 → 内层 coroutineScope 接住 → re-throw → 外层 try 捕获
                coroutineScope {
                    val f = async { delay(50); error("💥子异常"); 1 }
                    f.await()
                }
            } catch (e: Exception) {
                println("   ✅ catch 捕获: ${e.message}")
            }
            println("   launch-1 正常结束 ✅")
        }
        // 兄弟在外层 coroutineScope，是 launch-1 的兄弟
        // 不是内层 coroutineScope 的兄弟
        val normal = async { println("   兄弟尝试..."); delay(100); println("   兄弟完成 ✅"); 1 }
        normal.await()
        println("   外层 coroutineScope 正常结束 ✅")
    }
    println("   → 结论: 内层 coroutineScope 做防火墙，异常被隔离，兄弟存活\n")
}

/** #4 🟡 coroutineScope: async 定义在 coroutineScope 外部，防火墙失效 */
suspend fun asyncCoroutineScope_asyncOutsideFirewall() {
    println("\n=== #4 🟡 async + coroutineScope + async 定义在防火墙外部失效 ===")
    try {
        coroutineScope {  // 外层
            launch {
                try {
                    // ⚠️ async 定义在 coroutineScope 外部！
                    // async 的父 Job = 这个 launch，不是内层 coroutineScope
                    val f = async { delay(50); error("💥子异常"); 1 }
                    // coroutineScope 包的是 await，不是 async 本身
                    coroutineScope {
                        f.await()
                    }
                } catch (e: Exception) {
                    println("   ✅ catch 捕获值: ${e.message}")
                    println("   ⚠️ 但 launch 的 Job 已被取消...")
                }
                // launch 的 Job 已被取消，后续挂起点全死
                delay(50)
                println("   这行不会打印")
            }
            val normal = async { println("   兄弟尝试..."); delay(100); println("   兄弟完成? 不会打印"); 1 }
            normal.await()
        }
    } catch (e: CancellationException) {
        println("   💀 外层 coroutineScope 因取消而结束(CancellationException)")
    } catch (e: Exception) {
        println("   💀 外层 coroutineScope re-throw: ${e.message}")
    }
    println("   → 结论: async 在 coroutineScope 外面定义的，防火墙拦不住 Job 层级的传播\n")
}

/** #5 🟢 coroutineScope: try 包在 async 内部 — 内部catch阻止传播 */
suspend fun asyncCoroutineScope_innerTry() {
    println("\n=== #5 🟢 async + coroutineScope + try 包内部(内部catch阻止传播) ===")
    coroutineScope {
        val f = async {
            try { delay(50); error("💥子异常") }
            catch (e: Exception) { println("   ✅ catch 捕获: ${e.message}") }
            1
        }
        val n = async { println("   兄弟尝试..."); delay(100); println("   兄弟完成"); 1 }
        f.await()
        n.await()
    }
    println("   → 结论: 内部接住了，兄弟存活\n")
}


// ============================================================
//                     async × supervisorScope
// ============================================================

/** #6 🟢 supervisorScope: try 包在 await() 外部 */
suspend fun asyncSupervisorScope_tryAwait() {
    println("\n=== #6 🟢 async + supervisorScope + try 包 await 外部 ===")
    supervisorScope {
        val failing = async { delay(50); error("💥子异常"); 1 }
        val normal  = async { println("   兄弟尝试..."); delay(100); println("   兄弟完成 ✅"); 1 }
        try {
            failing.await()
        } catch (e: Exception) {
            println("   ✅ catch 捕获: ${e.message}")
        }
        normal.await()
        println("   后续代码正常执行 ✅")
    }
    println("   → 结论: 完美! 异常捕获 + 兄弟存活 + 后续代码正常\n")
}

/** #7 🟢 supervisorScope: try 包在 async 内部 */
suspend fun asyncSupervisorScope_innerTry() {
    println("\n=== #7 🟢 async + supervisorScope + try 包内部 ===")
    supervisorScope {
        val f = async {
            try { delay(50); error("💥子异常") }
            catch (e: Exception) { println("   ✅ catch 捕获: ${e.message}") }
            1
        }
        val n = async { println("   兄弟尝试..."); delay(100); println("   兄弟完成 ✅"); 1 }
        f.await()
        n.await()
    }
    println("   → 结论: 完美，兄弟不受影响\n")
}

/** #8 🔴 supervisorScope: async(CEH) —— CEH 对 async 无效! */
suspend fun asyncSupervisorScope_CEH() {
    println("\n=== #8 🔴 async + supervisorScope + CEH(无效!) ===")
    supervisorScope {
        val f = async(CEH) { delay(50); error("💥子异常"); 1 }  // CEH 被忽略
        val n = async { println("   兄弟尝试..."); delay(100); println("   兄弟完成 ✅"); 1 }
        n.await()
        try {
            f.await()
        } catch (e: Exception) {
            println("   ✅ 只能靠 try-catch await: ${e.message}")
        }
    }
    println("   → 结论: CEH 对 async 完全无效! 必须靠 await + try-catch\n")
}


// ============================================================
//                     async × runBlocking
// ============================================================

/** #9 🟡 runBlocking: try 包在 await() 外部 —— 经典陷阱! */
suspend fun asyncRunBlocking_tryAwait() {
    println("\n=== #9 🟡 async + runBlocking + try 包 await 外部 ——⚠️ 经典陷阱 ===")
    try {
        runBlocking {
            val failing = async { delay(50); error("💥子异常"); 1 }
            val normal  = async { println("   兄弟尝试..."); delay(100); println("   兄弟完成? 不会打印"); 1 }
            try {
                // catch 只捕获了值，Job 早已被取消
                failing.await()
            } catch (e: Exception) {
                println("   ✅ catch 捕获值: ${e.message}")
                println("   ⚠️ 我以为没事了...")
            }
            // 走到这里时 runBlocking 的 Job 已被取消
            // 挂起点检查到 Job 已取消 → 抛异常
            normal.await()
            println("   这行永远不会打印 💀")
        }
    } catch (e: CancellationException) {
        println("   💀 runBlocking 因取消而结束(CancellationException)")
    } catch (e: Exception) {
        println("   💀 runBlocking re-throw 了原始异常(${e::class.simpleName}: ${e.message})")
    }
    println("   → 结论: catch 只捕获了值，Job 早已被取消，runBlocking 内后续全死\n")
}

/** #10 🟢 runBlocking: try 包在 async 内部 — 内部catch阻止传播 */
suspend fun asyncRunBlocking_innerTry() {
    println("\n=== #10 🟢 async + runBlocking + try 包内部(内部catch阻止传播) ===")
    try {
        runBlocking {
            val f = async {
                try { delay(50); error("💥子异常") }
                catch (e: Exception) { println("   ✅ catch 捕获: ${e.message}") }
                1
            }
            val n = async { println("   兄弟尝试..."); delay(100); println("   兄弟完成"); 1 }
            f.await()
            n.await()
            println("   后续代码正常 ✅")
        }
    } catch (e: CancellationException) {
        println("   💀 runBlocking 因取消而结束(CancellationException)")
    } catch (e: Exception) {
        println("   💀 runBlocking re-throw 了原始异常(${e::class.simpleName}: ${e.message})")
    }
    println("   → 结论: 内部 try 阻止了异常传播，兄弟存活\n")
}


// ============================================================
//                     综合对比：三种作用域的区别
// ============================================================

/** 对比演示: 同一个 async 异常，三种作用域的表现 */
suspend fun compareScopes() {
    println("\n╔══════════════════════════════════════════════╗")
    println("║     综合对比: async 异常 × 三种作用域       ║")
    println("╚══════════════════════════════════════════════╝")

    // coroutineScope: 兄弟死
    println("\n── coroutineScope ──")
    try {
        coroutineScope {
            val f = async { delay(30); error("💥"); 1 }
            val n = async { println("   兄弟尝试..."); delay(80); println("   兄弟活着?"); 1 }
            try { f.await() } catch (e: Exception) { println("   捕获: ${e.message}") }
            n.await()
            delay(50)
            println("   能走到这吗?")
        }
    } catch (_: Exception) { }

    // supervisorScope: 完美
    println("\n── supervisorScope ──")
    supervisorScope {
        val f = async { delay(30); error("💥"); 1 }
        val n = async { println("   兄弟尝试...");  delay(80); println("   兄弟活着? ✅"); 1 }
        try { f.await() } catch (e: Exception) { println("   捕获: ${e.message}") }
        n.await()
        println("   后续代码正常 ✅")
    }

    // runBlocking: 同 coroutineScope 的陷阱
    println("\n── runBlocking ──")
    try {
        runBlocking {
            val f = async { delay(30); error("💥"); 1 }
            val n = async { println("   兄弟尝试..."); delay(80); println("   兄弟活着?"); 1 }
            try { f.await() } catch (e: Exception) {
                println("   捕获: ${e.message}")
                println("   ⚠️ 但我以为没事了...")
            }
            // 下面的 delay 是挂起点，检查到 Job 已取消，抛 CancellationException
            try {
                delay(50)
                println("   能走到这吗?")
            } catch (e: CancellationException) {
                println("   💀 delay 抛出 CancellationException, Job 已取消")
            }
        }
    } catch (_ : Exception) {

    }
    println("compareScopes结束")
}


// ============================================================
//                            main
// ============================================================

fun main() = runBlocking {
    println("╔══════════════════════════════════════════════╗")
    println("║    Kotlin 协程异常处理矩阵 — 全覆盖测试      ║")
    println("╚══════════════════════════════════════════════╝")

    // ============ launch 系列 ============
    println("\n\n████████████████ launch 系列 ████████████████")

    launchCoroutineScope_outerTry()       // #1  🟡
    launchCoroutineScope_innerTry()       // #2  🟢 ←内部catch阻止传播
    launchCoroutineScope_tryOutsideLaunch() // #3 🔴
    launchCoroutineScope_innerCoroutineScope() // #4 🟢 ←内部coroutineScope+try

    launchSupervisorScope_CEH()           // #5  🟢
    launchSupervisorScope_innerTry()      // #6  🟢
    launchSupervisorScope_tryOutsideLaunch() // #7 🔴
    launchSupervisorScope_innerCoroutineScope() // #8 🟢

    launchRunBlocking_CEH()              // #9  🟡 ←CEH只打印不阻止传播
    launchRunBlocking_innerTry()         // #10 🟢 ←内部catch阻止传播
    launchRunBlocking_outerTry()         // #11 🟡
    launchGlobalScope_CEH()              // #12 🟢

    // ============ async 系列 ============
    println("\n\n████████████████ async 系列 ████████████████")

    asyncCoroutineScope_outerTry()       // #1 🟡
    asyncCoroutineScope_tryAwait()       // #2 🟡
    asyncCoroutineScope_wrapCoroutineScope() // #3 🟢 ←coroutineScope防火墙
    asyncCoroutineScope_asyncOutsideFirewall() // #4 🟡 ←防火墙失效
    asyncCoroutineScope_innerTry()       // #5 🟢 ←内部catch阻止传播

    asyncSupervisorScope_tryAwait()      // #6 🟢
    asyncSupervisorScope_innerTry()      // #7 🟢
    asyncSupervisorScope_CEH()           // #8 🔴

    asyncRunBlocking_tryAwait()          // #9 🟡 ⚠️ 经典陷阱
    asyncRunBlocking_innerTry()          // #10 🟢 ←内部catch阻止传播

    // ============ 综合对比 ============
    compareScopes()

    println("\n═══════════════════════════════════════════")
    println("  全部 21 种组合测试完毕!")
    println("  🟢 = 12种 (内部catch / supervisorScope / coroutineScope防火墙)")
    println("  🟡 = 6种  (捕获了但兄弟已灭)")
    println("  🔴 = 3种  (完全没捕获)")
    println("═══════════════════════════════════════════\n")
}
