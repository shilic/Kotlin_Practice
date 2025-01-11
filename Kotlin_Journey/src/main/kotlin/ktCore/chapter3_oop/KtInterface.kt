package ktCore.chapter3_oop

interface KtInterface {

}
interface IFlyer {
    // kotlin中虽然可以定义字段，但是在内部确实通过 getSpeed() 实现的。见下边的实现类Eagle，
    val speed :Int
    // java中不可以定义没有初始值的字段，必须要有明确的初始值，在kotlin中通过 val 加 get 方式来实现一个常量。
    val height :Int
        get() = 5000
    // 以下代码报错
    // val weight : Int = 100
    // kotlin基于java6。从java8开始才支持接口有默认实现。故kotlin只能通过静态内部类的方式来让接口实现默认实现。因为kotlin接口中的属性是用方法实现的，
    // 接口方法
    fun kind()
    // 带有默认实现的方法.kotlin内部通过在接口中定义一个静态内部类的方式来为 fly() 方法提供实现的。
    fun fly(){
        println("我能飞")
    }
}
class Eagle : IFlyer{
    override val speed: Int
        get() = 100

    override fun kind() {
        println("我是鹰酱")
    }
    override fun fly() {
        println("我能飞更高")
    }

}