package ktCore.chapter3_oop.ktData

import ktCore.chapter3_oop.kt_oop.MyColorEnum
import org.junit.jupiter.api.Test

/**
 * 使用data关键字创建一个数据类。
 */
data class BirdData(var color : MyColorEnum, var weight : Double, var age : Int)

class DataTest{
    @Test
    fun test1(){
        val b1 = BirdData(color = MyColorEnum.BLUE, age = 5,weight = 1000.0)
        val b2 = BirdData(color = MyColorEnum.BLUE, age = 5,weight = 1000.0)
        println("b1 == b2 ? ${b1 == b2}")
        val b3 = b1.copy() // 使用拷贝
        b3.age = 7
        println("b1 = $b1, \nb3= $b3")
    }
    @Test
    fun test2(){
        val b1 = BirdData(color = MyColorEnum.BLUE, age = 5,weight = 1000.0)
        val (color, weight,age) = b1 // 必须按顺序填写
        println("color = $color , weight = $weight , age = $age , ")
    }
}