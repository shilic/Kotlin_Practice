package ktCore.ch03_oop.ktData

import ktCore.ch03_oop.kt_oop.MyColorEnum
import org.junit.jupiter.api.Test

/**
 * 使用data关键字创建一个数据类。
 */
data class BirdData(var color : MyColorEnum, var weight : Double, var age : Int)

class DataTest{
    /**
     * 自动生成的 equals()、hashCode() 和 toString() 方法
    Data 类：data 类会自动生成 equals()、hashCode() 和 toString() 方法。这些方法会根据类的属性生成，确保对象的相等性和哈希值是基于其属性值的
     */
    @Test
    fun test1(){
        val b1 = BirdData(color = MyColorEnum.BLUE, age = 5,weight = 1000.0)
        val b2 = BirdData(color = MyColorEnum.BLUE, age = 5,weight = 1000.0)
        println("b1 == b2 ? ${b1 == b2}") // equals()
        // Data 类：data 类会自动生成 copy() 方法，允许你创建对象的副本并修改某些属性。
        val b3 = b1.copy() // 使用拷贝
        b3.age = 7
        println("b1 = $b1, \nb3= $b3") //  toString()
    }
    @Test
    fun test2(){
        val b1 = BirdData(color = MyColorEnum.BLUE, age = 5,weight = 1000.0)
        /*自动生成的组件函数
        Data 类：data 类会自动生成 componentN() 函数。这些函数允许你通过解构声明（destructuring declarations）来访问对象的属性。
        你可以这样使用：
        * */
        val (color, weight,age) = b1 // 必须按顺序填写
        println("color = $color , weight = $weight , age = $age , ")
    }
}