package ktCore.chapter3_oop.ktData


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource


/**
 * 用于测试kotlin中的 when语句
 */
class WhenTest{
    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3])
    fun test1 (a :Int ) {
        // when语句用于替换if else 和 switch 语句。不再需要 break语句
        var value : String = "intel"
        when (a)  {
            // when语句，会每一个逐个匹配，类似于 switch
            1 -> {
                // 语句的分支可以是表达式
                value = "intel"
                println("intel")
            }
            0 -> {
                value = "motorola"
                println("motorola")
            }
            // when语句中的else相当于 default 分支
            else ->{
                println("default")
            }
        }
        //return value
    }

    /**
     * 这里可以存入参数以及预期值 expected
     */
    @ParameterizedTest
    @CsvSource(
        "1, intel",
        "0, motorola",
        "2,  default"
    )
    fun testGetValue(a: Int, expected: String) {
        // when语句用于替换if else 和 switch 语句。不再需要 break语句
        val value: String
        when (a)  {
            // when语句，会每一个逐个匹配，类似于 switch
            1 -> {
                // 语句的分支可以是表达式
                value = "intel"
                //println("intel")
            }
            0 -> {
                value = "motorola"
                //println("motorola")
            }
            // when语句中的else相当于 default 分支
            else ->{
                value = "default"
                //println("default")
            }
        }
        //return value
        // 这里使用 assertEquals 断言，替代了返回值
        assertEquals(expected, value)
    }
    @Test
    fun test2() {
        val a = 5
        // when 语句可以有返回值
        val value : String = when (a) {
            // 语句的分支也可以是返回值 , 返回值类型就是所有分支语句中最后一行的类型,所有分支的返回值类型需要相同,例如 "intel" 就是 String 类型
            1 -> {
                println("intel")
                "intel"
            }
            0 -> {
                println("motorola")
                "motorola"
            }
            // when语句中的else相当于 default 分支
            else -> {
                println("default")
                "default"
            }
        } // when
        println("value=$value")
    }
    @Test
    fun test3(){
        //多个条件,你可以将多个条件用逗号分隔，放在同一个分支中：
        val x = 10
        when (x) {
            0, 1 -> println("Zero or One")
            else -> println("Other")
        }

        //范围匹配,Kotlin 支持范围匹配，可以检查值是否在某个范围内：
        val y = 5
        when (y) {
            in 1..10 -> println("x is between 1 and 10")
            in 11..20 -> println("x is between 11 and 20")
            else -> println("x is outside the range")
        }
    }
    // 类型检查 , when 表达式还可以用于类型的匹配和智能转换：
    @Test
    fun printType(x: Any) {

        when (x) {
            is String -> println("x is a string")
            is Int -> println("x is an integer")
            else -> println("Unknown type")
        }
    }
    @Test
    fun test4(){
        // 常量匹配 , when 表达式可以匹配常量：
        val DAY = 3
        val dayName = when (DAY) {
            1 -> "Monday"
            2 -> "Tuesday"
            3 -> "Wednesday"
            4 -> "Thursday"
            5 -> "Friday"
            6 -> "Saturday"
            7 -> "Sunday"
            else -> "Invalid day"
        }
        println(dayName)  // 输出: Wednesday
    }
    @Test
    fun test5(){
        //无表达式的 when .when 也可以不带表达式，类似于 if 语句，用于检查条件：
        val x = 10
        when {
            x < 0 -> println("Negative")
            x == 0 -> println("Zero")
            else -> println("Positive")
        }
    }

}


