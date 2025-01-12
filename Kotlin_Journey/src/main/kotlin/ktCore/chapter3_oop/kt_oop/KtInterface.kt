package ktCore.chapter3_oop.kt_oop

import org.junit.jupiter.api.Test

interface KtInterface {

}
interface IFlyer {
    // kotlin中虽然可以定义字段，但是在内部确实通过 getSpeed() 实现的。见下边的实现类Eagle，实现类必须手动实现get方法。
    // 实际上kotlin中的接口并没有常量，故kotlin中通过一个get方法来实现了接口常量。
    val speed :Int
    // java中不可以定义没有初始值的字段，必须要有明确的初始值，在kotlin中通过 val 加 get 方式来实现一个常量。
    val height :Int
        get() = 5000
    // 以下代码报错
    // val weight : Int = 100
    // kotlin基于java6。从java8开始才支持接口有默认实现。故kotlin只能通过静态内部类的方式来让接口实现默认实现。因为kotlin接口中的属性是用方法实现的，
    // 接口方法
    fun kind() : String{
        return "类型是飞行动物"
    }
    // 带有默认实现的方法.kotlin内部通过在接口中定义一个静态内部类的方式来为 fly() 方法提供实现的。
    fun fly()
    fun eat()
}
interface ISwim{
    fun swim()
}
class Eagle : IFlyer {
    // 接口的实现必须要用get声明常量值
    override val speed: Int
        get() = 100

    override fun kind() :String {
        return "我是鹰酱"
    }

    /**
     * 可以重写一个有默认实现的接口方法
     */
    override fun fly() {
        println("我能飞更高")
    }

    override fun eat() {
        println("我吃野兔")
    }

}
interface IAnimal {
    val name :String
    fun eat()
    fun kind() = "类型是动物"
}
/** 通过接口方式实现多继承 */
class Falcon(speed: Int, override val name: String) : IFlyer, IAnimal {
    override val speed: Int = speed // 可以通过把属性放到里边来定义，而不是默认构造函数。

    /** 如果是没有默认实现的 抽象方法，直接实现即可 */
    override fun eat() {
        println("我吃小鸡")
    }

    /**
     * 多继承问题，拥有相同的方法名，可用 super 来指定，如 super<IFlyer>.kind()
     */
    override fun kind(): String {
        return super<IFlyer>.kind()
    }

    override fun fly() {
        println("我能飞更快")

    }
}


/* 通过委托的方式实现多继承,需要两个已经实现好了的类 */
open class Flyer(override val speed: Int) : IFlyer {
    override fun fly() {
        println("我能飞")
    }

    override fun eat() {
        println("我能吃")
    }

}
open class Swim : ISwim {
    override fun swim(){
        println("我能游泳")
    }

}

/**
 * 通过委托的方式实现多继承
 */
class Goose(flyer : IFlyer, swim : ISwim) : IFlyer by flyer, ISwim by swim {


}
class GooseTest{
    @Test
    fun gooseTest() {
        val flyer = Flyer(80)
        val swim = Swim()
        val goose : Goose = Goose(flyer,swim)
        goose.fly()
        goose.swim()
    }
}

