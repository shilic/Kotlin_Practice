package ktCore.chapter3_oop.kt_oop

class KtObject {

}
class Prize(val name:String,val count:Int,val type:Int){

    fun test1(){
        println("这是一个测试方法")
    }
    /**
     * 伴生类，实际上就是java中的 static 关键字
     */
    companion object{
        const val TYPE_REDPACK = 0
        const val TYPE_COUPON = 1
        fun isRedPack(prize: Prize):Boolean {
            return prize.type == TYPE_REDPACK
        }
    }
}

/**
 * object 创建的单例，会在系统加载的时候初始化，不需要手动初始化
 */
object ServiceManager{
    var name : String = "名字"
    var param1 :Int = 5
    var type :String = "类型"
}

fun main() {
    // 像static 关键字一样，使用 Prize.TYPE_REDPACK 来使用一个静态的变量
    val prize = Prize("红包",10,Prize.TYPE_REDPACK)
    println("Prize.isRedPack = ${Prize.isRedPack(prize)} " ) // 像使用静态方法一样使用
    // 不需要构造器来初始化，就可以直接使用的单例模式
    ServiceManager.name = "新名字"

    //你也可以手动用于创建一个匿名内部类
    // 可以用于实现一个接口
    val bird : IFlyer = object : IFlyer {
        override val speed: Int
            get() = 80

        override fun fly() {
            println("我能飞")
        }

        override fun eat() {
            println("我吃野兔")
        }

    }
    bird.fly()
}
