package ktCore.chapter4_ADT_Pattern.pattern

class PatternTest {
}
// 逻辑表达式匹配
fun logicPattern(a: Int) = when {
    a in 2..11 -> (a.toString() + " is smaller than 10 and bigger than 1")
    else -> "Maybe" + a + "is bigger than 10, or smaller than 1"
}
// 逻辑表达式匹配
fun logicPattern(a: String) = when {
    a.contains("Yison") -> "Something is about Yison"
    else -> "It`s none of Yison`s business"
}
// 数字或者操作
sealed class Expr {
    data class Num(val value: Int) : Expr()
    data class Operate(val opName: String, val left: Expr, val right: Expr) : Expr()
}

// 使用if else 十分复杂
fun simplifyExpr(expr: Expr): Expr = if (expr is Expr.Num) {
    expr
} else if (expr is Expr.Operate && expr.opName == "+" && expr.left is Expr.Num && expr.left.value == 0) {
    expr.right
} else if (expr is Expr.Operate && expr.opName == "+" && expr.right is Expr.Num && expr.right.value == 0) {
    expr.left
} else expr

// 同样是逻辑判断
fun simplifyExpr1(expr: Expr): Expr = when {
    (expr is Expr.Operate) && (expr.opName == "+") && (expr.left is Expr.Num) && (expr.left.value == 0) -> expr.right
    (expr is Expr.Operate) && (expr.opName == "+") && (expr.right is Expr.Num) && (expr.right.value == 0) -> expr.left
    else -> expr
}

/**
 * 这里利用了反向构造 val expr = Expr.Operate("+", Expr.Num(0), expr.right) 是通过构造函数生成一个对象。
 * 而 val
 */
fun test1(): Unit {
    val x = Expr.Num(5)
    val expr = Expr.Operate("+", Expr.Num(0), x)
    //val Expr.Operate("+", Expr.Num(0), x)  = exp
}
// 以下代码暂时报错
fun simplifyExpr2(expr: Expr): Expr = when (expr) {
    is Expr.Num -> expr
    is Expr.Operate -> when (expr) {
        // 使用了一个反向构造，将 expr拆分成对应的构造函数
        Expr.Operate("+", Expr.Num(0), expr.right) -> expr.right
        Expr.Operate("+", expr.left, Expr.Num(0)) -> expr.left
        else -> expr
    }
    else -> { expr}
}

fun simplifyExpr3(expr: Expr): Expr = when (expr) {
    is Expr.Num -> expr
    is Expr.Operate -> when (expr) {
        Expr.Operate("+", Expr.Num(0), expr.right) -> simplifyExpr(expr.right)
        Expr.Operate("+", expr.left, Expr.Num(0)) -> expr.left
        else -> expr
    }
    else ->  { expr}
}

fun simplifyExpr4(expr: Expr): Expr = when (expr) {
    is Expr.Num -> expr
    is Expr.Operate -> when {
        (expr.left is Expr.Num && expr.left.value == 0) && (expr.right is Expr.Operate) ->
            when (expr.right) {
                Expr.Operate("+", expr.right.left, Expr.Num(0)) -> expr.right.left
                else -> expr.right
            }
        else -> expr
    }
    else ->  { expr}
}