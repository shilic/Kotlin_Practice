package ktCore.chapter3_oop

import org.junit.Test


class KtObject {

}

class Bird() {
    // kotlin中默认所有变量都需要显示初始化值，即便是初始化null值。否则编译报错。而java中则每个基本类型都定义有默认值，不用显示赋值。或者可以使用 lateinit var表示延迟赋值。
    // kotlin中推荐使用 val来定义属性和局部变量，内部使用了java的final字段来实现。val修饰的基本数据类型不可修改，而引用类型则可以修改
    val color : MyColorEnum = MyColorEnum.BLUE
    // var 则表示可变变量
    var weight : Double = 500.0

    val type :String = "麻雀"
    var name :String = "小羽"
        set(value) {
            field = value.trim()
        }

    var call : String = "叽叽喳喳"
        // 只有当你需要对 输入输出进行特殊化处理的时候，才需要手动修改 set 和 get 方法。其他的时候 由kotlin 自动实现。
        get() {
            //get和set方法为kotlin默认实现，不需要自己手动实现。在代码中通过 bird.name 和 bird.call直接使用，也不用加 set 。
            return "~~~~$field######"
        }
        private set

    var age : Int = 1
    // kotlin中的成员默认全部为 public 可见，除非显示定义为 private 。这一点和java有所区别
    private var describe : String = "这是麻雀"
    // 延迟赋值的属性
    lateinit var parent : Bird
    lateinit var child : Array<Bird>
    // 动态方法
    fun fly(){
        println("I can fly")
    }
}
// kotlin 中的函数是头等公民，函数和类可以平起平坐，不像java一样必须在类中定义方法。这是函数式编程的一大特性。
fun printObjectTest(){
    println("这里是KtObject类")
}
// Cannot access 'accessTest2': it is private in file 。导入一个 private 方法直接标红。
private fun accessTest2(){
    println("测试一下在类外部使用 private 修饰的方法，其他包可以引用吗")
}

class BirdTest{
    fun test(){
        println("BirdTest")
    }
    /**
     * 测试 set 和 get的一些用法
     */
    @Test
    fun getAndSetTest(){
        val bird = Bird()
        println("${bird.name}怎么叫：${bird.call}") // bird.call 这里实际上使用了 get语句，只不过 kotlin 简化了
        // 下边这句代码会报错。set 被私有化了之后，将无法从外部赋值。刚才说了，kotlin中的属性默认全部都是 public 的，包括 get 和 set 也是默认实现了的
        // bird.call = "呜呜呜"
        val oldName = bird.name
        val inputName = "   小风    "
        bird.name =  inputName // 这里实际上是使用了 set 语句，只不过 kotlin 同样简化了操作
        println("原来的名字$oldName，输入 '$inputName' 之后，改名为${bird.name}") // 可以看到，在内部调用set语句后，对输入做了一些处理
        // 尝试对一个 val 变量赋值 ，编译器会直接标黄报错。
        // bird.type = "游隼"
    }
}


