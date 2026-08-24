package ktCore.ch06_lambda_list.ktLambda

// 经典的有副作用的函数。
// 工厂函数，每次调用返回一个新的计数器闭包
fun makeCounter(): () -> Int {
    var count = 0          // 局部变量被 lambda 捕获（可变引用）
    return { ++count }     // 每次调用递增并返回
}

fun main() {
    val counter1 = makeCounter()
    println(counter1()) // 1
    println(counter1()) // 2
    println(counter1()) // 3

    val counter2 = makeCounter()
    println(counter2()) // 1 (独立于 counter1)
    println(counter2()) // 2

    // 验证：同一个闭包每次结果不一致，不同闭包互不影响
}