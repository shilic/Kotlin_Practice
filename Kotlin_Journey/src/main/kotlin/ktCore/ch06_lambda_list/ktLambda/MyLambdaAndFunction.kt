package ktCore.ch06_lambda_list.ktLambda

import org.junit.jupiter.api.Test

class MyLambda {

}
class KtMethod {
    /** 函数中可以有函数 */
    fun outFunction(){
        fun double(y:Int):Int{
            return y*2
        }
    }
    /** 普通的函数类型。 函数定义使用关键字 fun，参数格式为：参数 : 类型 */
    fun sum0(a: Int, b: Int): Int {   // Int 参数，返回值 Int
        println("sum0 和是 ${(a+b)}" )
        return a + b
    }

    /** 表达式作为函数体，返回类型自动推断 */
    fun sum1(a: Int, b: Int) = {
        println("sum1 和是 ${(a+b)}" )
        //println("又做了一些操作")
        a + b
    }

    /** 使用 Lambda 表达式来定义一个函数，也就是使用 fun 关键字和 = 来定义 。显示定义返回值类型
     * sum2的返回值是一个 lambda 表达式 () → Int ，不是一个 Int 返回值
     * */
    fun sum2(a: Int, b: Int): () -> Int = {
        println("sum2 和是 ${(a+b)}" )
        a + b
    }

    /** 使用 run 函数，定义一个函数  */
    fun sum3(a: Int, b: Int) = run {
        println("run表达式 ，sum3 和是 ${(a+b)}" )
        a + b
    }
    /** 你也可以通过直接把参数定义到 lambda 中的方式实现 */
    fun sum4() : (Int, Int)-> Int = { a: Int, b: Int ->
        println("sum4 和是 ${(a+b)}" )
        a + b
    }


    /** 使用 lambda 表达式 定义一个 函数，使用 val 关键字 和  lambda 表达式 */
    val sumLambda1: (Int, Int) -> Int = {
            x,y ->
        println("sumLambda 和是 ${( x + y )}" )
        x+y
    }
    /** 使用 lambda 表达式 定义一个 函数，使用 val 关键字 和  lambda 表达式 */
    val sumLambda2 : (Int, String) -> String  = { i: Int, str : String ->
        val re :String
        when(i){
            1 -> {
                re = "inter"
                re + str
            }
            0 -> {
                re = "motorola"
                re + str
            }
            else -> "未找到"
        }
    }




    /** 将函数作为一个参数 */
    fun useSum(a:Int , b:Int, sum : (Int,Int)->Int ) : Int {
        return sum.invoke(a,b)
    }
}

class MethodTest(){
    val m = KtMethod()
    @Test
    fun methodTest(){
        // 直接采用函数表达式运行
        val re1  = m.sumLambda2(1,"  类型")
        println(re1)
    }
    @Test
    fun invokeTest(){
        // lambda 代码块 可以通过 invoke 运行
        {
            println("invoke直接运行")
        }.invoke()
        // 也可以通过函数体的方式 加 invoke运行
        val re1 = m.sum1(1,1)
        println("运行： m.sum1(1,1) ，结果：${re1}。\n")
        val re2 = m.sum1(1,1).invoke()
        println("运行  m.sum1(1,1).invoke() ，结果：${re2}。\n")

        val re3 = m.sum2(1,1)
        println("运行  m.sum2(1,1) ，结果：${re3}。\n")
        val re4 = m.sum2(1,1).invoke()
        println("运行  m.sum2(1,1).invoke() ，结果：${re4}。\n")
        // 可见，如果采用 fun 加 = 定义的 lambda风格 的函数 ，结果是 Function0<java.lang.Integer> ，返回值是一个函数类型，内部的代码也并没有执行。
        // 也就是说， lambda 风格的函数，必须使用 invoke() 执行。

        val re5 = m.sum4().invoke(2,3)
        println("运行  m.sum4().invoke(2,3)，结果：${re5}。\n")

        // run表达式可以直接运行
        val re6 = m.sum3(1,1)
        println("运行  m.sum3(1,1) ，结果：${re6}。\n")
    }
    @Test
    fun useSumTest(){
        // 如果将函数作为参数传入 另外一个函数，需要使用双冒号语法，对方法进行引用，否则传入的就是函数的返回值
        m.useSum(1,1,m::sum0)
        // m.useSum(1,1,m::sum1) // 报错,sum2同样报错
        m.useSum(1,1,m::sum3)

    }

}
