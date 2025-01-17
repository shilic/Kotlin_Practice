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
    fun sum0(a: Int, b: Int): Int {   // Int 参数，返回值 Int // kotlin.Int，
        println("sum0 和是 ${(a+b)}" )
        return a + b
    }

    /** sum1 使用 Lambda 表达式来定义一个函数，也就是使用 fun 关键字和 = 来定义 。返回值类型自动推断 <br>
     * sum1 的返回值是一个 lambda 表达式 () → Int ，不是一个 Int 返回值。<br>
     * 使用 m.sum1(1,1).invoke() 来执行
     * */
    fun sum1(a: Int, b: Int) = { // () -> kotlin.Int，
        println("sum1 和是 ${(a+b)}" )
        //println("又做了一些操作")
        a + b
    }

    /** sum2 使用 Lambda 表达式来定义一个函数，也就是使用 fun 关键字和 = 来定义 。显式 定义返回值类型<br>
     * sum2的返回值是一个 lambda 表达式 () → Int ，不是一个 Int 返回值 <br>
     * 使用 m.sum2(1,1).invoke() 来执行
     * */
    fun sum2(a: Int, b: Int): () -> Int = { // () -> kotlin.Int，
        println("sum2 和是 ${(a+b)}" )
        a + b
    }

    /** 使用 run 函数，定义一个函数 ，返回值 kotlin.Int 。具体使用和普通函数 sum0 一致  */
    fun sum3(a: Int, b: Int) = run {
        println("run表达式 ，sum3 和是 ${(a+b)}" )
        a + b
    }
    /** 你也可以通过直接把参数定义到 lambda 中的方式实现。 <br> 返回值： (kotlin.Int, kotlin.Int) -> kotlin.Int 。<br>
     * 具体执行 由 invoke 去执行 。 m.sum4().invoke(2,2)  */
    fun sum4() : (Int, Int)-> Int = {
            a, b ->
        println("sum4 和是 ${(a+b)}" )
        a + b
    }
    /** sum5 的写法，虽然使用了等号，但是没有使用花括号，故用法和普通函数sum0一致，  */
    fun sum5(a: Int, b: Int) = a + b
    /**  sum6 写法和 sum4如出一辙 。返回值： (kotlin.Int, kotlin.Int) -> kotlin.Int。只不过把参数定义放里边了 */
    fun sum6() = {
            a:Int, b:Int ->
        println("sum4 和是 ${(a+b)}" )
        a + b
    }
    /*  sum4 的写法等价于 sumLambda1 的写法，返回值都是 (kotlin.Int, kotlin.Int) -> kotlin.Int  */
    /** 柯里化风格编程 */
    fun sum7(a:Int) = {
        b:Int -> {
            a + b
        }
    }

    /** 使用 lambda 表达式 定义一个 函数，使用 val 关键字 和  lambda 表达式 。<br>
     * 返回值 :(kotlin.Int, kotlin.Int) -> kotlin.Int <br>
     * 你可以直接运行，re7 = m.sumLambda1(1,6) ; 也可以通过 invoke 运行 m.sumLambda1.invoke(1,7) ;
     * */
    val sumLambda1: (Int, Int) -> Int = {
            x,y ->
        println("sumLambda(参数定义放外边) 和是 ${( x + y )}" )
        x+y
    }

    /** 和 sumLambda1 如出一辙。 */
    val sumLambda2 = {
            x:Int,y:Int ->
        println("sumLambda(参数定义放里边) 和是 ${( x + y )}" )
        x+y
    }

    /** 使用 lambda 表达式 定义一个 函数，使用 val 关键字 和  lambda 表达式 */
    val sumLambda3 : (Int, String) -> String  = {
            i: Int, str : String ->
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



    fun reduce(a:Int ,b: Int) :Int {

        return a-b
    }



    /** 将函数作为一个参数 */
    fun useSum(a:Int , b:Int, sum : (Int,Int)->Int ) : Int {
        return sum.invoke(a,b)
    }

    /** 将函数作为一个返回值getOperation 是一个高阶函数，它接受一个 String 参数 operation，并返回一个函数类型 (Int, Int) -> Int。<br>
    在 getOperation 中，我们使用 when 表达式来根据 operation 的值返回不同的函数引用（::sum0 或 ::reduce）。 */
    fun getOperation(operation: String): (Int, Int) -> Int {
        return when (operation) {
            "sum" -> ::sum0
            "reduce" -> ::reduce
            else -> throw IllegalArgumentException("不支持的操作")
        }
    }
}
/**
 * 使用一个 Int 来接收这个函数
 */
val sumLambda4 : Int.(Int) -> Int = {
        other -> this + other // other -> plus(other)
}
class MethodTest(){
    val m = KtMethod()
    @Test
    fun methodTest(){
        // 直接采用函数表达式运行
        val re1  = m.sumLambda3(1,"  类型")
        println(re1)
    }
    @Test
    fun invokeTest(){
        {x:Int -> println(x)}(1) ; // 直接运行的语法
        // lambda 代码块 可以通过 invoke 运行
        {
            println("invoke直接运行")
        }.invoke()
        // 也可以通过函数体的方式 加 invoke运行
        val re1 = m.sum1(1,0)
        println("运行： m.sum1(1,0) ，结果：${re1}。\n")  // Function0<java.lang.Integer>
        val re2 = m.sum1(1,0).invoke()
        println("运行  m.sum1(1,0).invoke() ，结果：${re2}。\n")

        val re3 = m.sum2(1,1)
        println("运行  m.sum2(1,1) ，结果：${re3}。\n") // Function0<java.lang.Integer>
        val re4 = m.sum2(1,1).invoke()
        println("运行  m.sum2(1,1).invoke() ，结果：${re4}。\n")
        // 可见，如果采用 fun 加 = 定义的 lambda风格 的函数 ，结果是 Function0<java.lang.Integer> ，返回值是一个函数类型，内部的代码也并没有执行。
        // 也就是说， lambda 风格的函数，必须使用 invoke() 执行。

        // 例外。 run表达式可以直接运行
        val re6 = m.sum3(1,2)
        println("运行  m.sum3(1,2) ，结果：${re6}。\n")

        // 只能在 invoke 中运行
        val re5 = m.sum4().invoke(2,2)
        println("运行  m.sum4().invoke(2,2)，结果：${re5}。\n")

        val re8 = m.sum5(2,3) // 使用等号定义的函数，可以直接使用。没有大括号，不是 lambda语法。
        //val re9 = m.sum5(2,3).invoke() // 没有大括号，就无法调用 invoke 函数，报错

        // 使用 val 定义的函数，可以直接运行
        val re7 = m.sumLambda1(1,6)
        println("运行   m.sumLambda1(1,6)，结果：${re7}。\n")

        m.sumLambda1.invoke(1,7) // 同样，你也可以在 invoke中运行
    }

    /**
     * 函数的返回值类型。
     */
    @Test
    fun methodType(){
        val method0 = m::sum0
        val method1 = m::sum1
        val method2 = m::sum2
        val method3 = m::sum3
        val method4 = m::sum4
        m::sum5 // 返回值 Int
        m::sum6 // 返回值  (Int, Int) → Int，和  m::sum4一致
        val lambda1 = m::sumLambda1 // 返回值 (Int, Int) → Int
        m::sumLambda2 // 返回值 (Int, Int) → Int
        val returnTypeString = """
        method0 的returnType类型：${method0.returnType}，
        method1 的returnType类型：${method1.returnType}，
        method2 的returnType类型：${method2.returnType}，
        method3 的returnType类型：${method3.returnType}，
        method4 的returnType类型：${method4.returnType}，
        lambda1 的returnType类型：${lambda1.returnType}，
        """.trimIndent()
        println(returnTypeString)
        /*
        * method0 的returnType类型：kotlin.Int，
        method1 的returnType类型：() -> kotlin.Int，
        method2 的returnType类型：() -> kotlin.Int，
        method3 的returnType类型：kotlin.Int，
        method4 的returnType类型：(kotlin.Int, kotlin.Int) -> kotlin.Int，
        lambda1 的returnType类型：(kotlin.Int, kotlin.Int) -> kotlin.Int，
        * */
    }

    /**
     * 函数作为一个参数
     */
    @Test
    fun useSumTest(){
        // 如果将函数作为参数传入 另外一个函数，需要使用双冒号语法，对方法进行引用，否则传入的就是函数的返回值
        m.useSum(1,1,m::sum0)
        //m.useSum(1,1,m::sum1.invoke()) // 报错
        //m.useSum(1,1,m::sum2.invoke())  // 报错,sum2同样报错 。编译器提示 ：需要: (Int, Int) → Int ，已找到:() → Int
        m.useSum(1,2,m::sum3)
        /* 可见如果要让 函数作为参数 ，就必须是函数类型，而不是 lambda 类型的函数。例如 sum0 和 sum3 */

        // 看下边，如果是一个 lambda 表达式格式定义的函数,可以加上 .call()来传入。m::sum4.invoke()也可以
        m.useSum(1,3,m::sum4.invoke()) // 运行通过

        // 匿名函数
        m.useSum(1,4,fun (a:Int,b:Int) :Int {
            println("匿名函数 和是 ${(a+b)}" )
            return a + b
        })
        // 直接传入一个 lambda 表达式 的 匿名函数
        m.useSum(1,5,{
                a, b ->
            println("lambda1 和是 ${(a+b)}" )
            a + b
        })
        // 如果 lambda 表达式在函数参数的末尾，那么就可以放到外边来写，俗称柯里化风格的函数调用
        m.useSum(1,6) { a:Int, b:Int -> // Int 类型可以省略
            println("lambda2 和是 ${(a + b)}")
            a + b
        }
       // m.useSum(1,3,m::sum5) // 运行通过。不使用大括号，就不是 lambda 语法
    }
    @Test
    fun operationTest(){
        // 获取加法操作
        val sumOperation = m.getOperation("sum")
        println("Sum 结果: ${sumOperation(5, 3)}")

        // 获取减法操作
        val reduceOperation = m.getOperation("reduce")
        println("Reduce 结果: ${reduceOperation(5, 3)}")
    }
    @Test
    fun test(){
        val res1 = 2.sumLambda4(3) // 使用扩展
        println(res1) // 5
    }

}
