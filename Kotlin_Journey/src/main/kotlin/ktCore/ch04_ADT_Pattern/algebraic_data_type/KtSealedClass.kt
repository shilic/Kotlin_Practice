package ktCore.ch04_ADT_Pattern.algebraic_data_type

import org.junit.jupiter.api.Test

// 密封类是和类型
sealed class MyDay {
    class SUN : MyDay(){

    }
    class MON : MyDay()
    class TUE : MyDay()
    class WED : MyDay()
    class THU : MyDay()
    class FRI : MyDay()
    class SAT : MyDay()
}

sealed class Shape{
    class Circle(val radius:Double): Shape()
    class Rectangle(val width:Double,val height:Double): Shape()
    class Triangle(val base:Double,val height:Double): Shape()

}

class SealedTest(){
    @Test
    fun test1(){
        val day = MyDay.SUN()
        schedule(day)
    }
    // 密封类因为是类型安全的，子类数量确定，所以可以很好的使用when表达式
    fun schedule(day : MyDay){
        when(day){
            is MyDay.SUN -> {
                println("星期天")
            }
            is MyDay.MON -> {
                println("星期一")
            }
            is MyDay.TUE -> {
                println("星期二")
            }
            is MyDay.WED -> {
                println("星期三")
            }
            is MyDay.THU -> {
                println("星期四")
            }
            is MyDay.FRI -> {
                println("星期五")
            }
            is MyDay.SAT -> {
                println("星期六")
            }
        }
    }
    // 如果使用和类型添加到when语句中，因为类型是确定的，所以不用添加else语句
    fun getArea(shape: Shape):Double = when(shape) {
        is Shape.Circle -> Math.PI * shape.radius * shape.radius
        is Shape.Rectangle -> shape.width * shape.height
        is Shape.Triangle -> shape.base * shape.height / 2
    }
}