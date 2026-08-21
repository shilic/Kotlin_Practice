package ktCore.ch06_lambda_list.ktLambda


fun localFunctionTest() {
    fun funcBefore() {
        println("funcBefore")
    }

    funcBefore()
    // 以下代码报错，本地函数实际上是编译成外部函数的局部变量，
    // 所以必须按顺序定义和使用，不可以在后边定义，然后在前边使用。
    // funcAfter()

    fun funcAfter() {
        println("funcAfter")
    }

}

fun main() {
    localFunctionTest()
}