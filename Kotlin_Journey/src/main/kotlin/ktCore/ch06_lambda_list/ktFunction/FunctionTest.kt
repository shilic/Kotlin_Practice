package ktCore.ch06_lambda_list.ktFunction

infix fun <A, B, C> ((A) -> B).andThen(other: (B) -> C): (A) -> C = { other(this(it)) }

fun addOne(value: Int): Int {
    return value + 1
}

fun doubleValue(value: Int): Int {
    return value * 2
}

val composedFunction: (Int) -> Int = ::addOne andThen ::doubleValue

fun main() {
    val result = composedFunction(5)
    println(result) // 输出: 12 (5 + 1 = 6, 6 * 2 = 12)
}