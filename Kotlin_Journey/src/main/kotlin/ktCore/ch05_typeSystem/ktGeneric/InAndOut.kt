package ktCore.ch05_typeSystem.ktGeneric


interface Consumer<in T>  {
    fun consumer(value : T): Boolean
}
open class Fruit2(val weight: Double = 0.0) {
    fun eatFruit(){println("eat Fruit")}
}
class Apple2(weight: Double = 0.0): Fruit2(weight){
    fun eatApple() { println("eat Apple") }
}
class Banana2(weight: Double = 0.0): Fruit2(weight){
    fun eatBanana() {
        println("eat Banana")
    }
}
fun main(args: Array<String>) {
    // 1. 创建了一个 Consumer<Fruit2> 的匿名实现 — 这是唯一存在的对象
    val fruitConsumer: Consumer<Fruit2> = object : Consumer<Fruit2> {
        override fun consumer(value : Fruit2): Boolean {
            println("fruitConsumer ${value.weight}")
            return (value.weight > 4)
        }
    }
    // 2. 逆变：Consumer<Fruit2> 赋给 Consumer<Apple2>
    // 编译器层面 — in 限制了 Consumer<Apple2> = fruitConsumer 只能用于输入 Apple2，不允许把 Apple2 读出来
    // 运行时层面 — 实际跑的永远是 Consumer<Fruit2> 的代码，传入的 Apple2 只是被当作 Fruit2 用，不会访问到 Apple2 专属的方法
    val appleConsumer: Consumer<Apple2> = fruitConsumer
    // 声明类型 Consumer<Apple2> 认为参数是 Apple2
    // → 实际调用 fruitConsumer.consumer(Apple2())
    // → Fruit2 没问题，Apple2 也是 Fruit2 ✅
    val v : Boolean = appleConsumer.consumer(Apple2(5.0))
    // 逆变（in）的核心直觉：你有一个能处理"宽类型"的处理器，
    // 把它当作只处理"窄类型"的处理器用——你传进去的永远是窄类型的子集，处理器本就能处理更宽的范围，所以绝不会出错。
    println(v)
}