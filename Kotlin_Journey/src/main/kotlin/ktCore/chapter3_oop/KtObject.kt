package ktCore.chapter3_oop

import org.junit.Test
import sun.text.resources.cldr.om.FormatData_om


class KtObject {

}
// 需要显式的使用open指定可以被继承
// Bird 类只有一个构造函数，无参构造函数，我们需要添加其他构造函数。在java中，我们需要手动通过构造函数的重载来实现多个构造函数，而kotlin则不需要
abstract class Bird() {
    init {
        println("鸟类正在被创建")
    }
    // kotlin中默认所有变量都需要显示初始化值，即便是初始化null值。否则编译报错。而java中则每个基本类型都定义有默认值，不用显示赋值。或者可以使用 lateinit var表示延迟赋值。
    // kotlin中推荐使用 val来定义属性和局部变量，内部使用了java的final字段来实现。val修饰的基本数据类型不可修改，而引用类型则可以修改
    open var color : MyColorEnum = MyColorEnum.BLUE
    // var 则表示可变变量
    var weight : Double = 777.7
    var age : Int = 7

    abstract var type :String?
    abstract fun returnType() :String
    abstract var gender : GenderEnum?


    open var name :String = "小羽"
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

    // kotlin中的成员默认全部为 public 可见，除非显示定义为 private 。这一点和java有所区别
    private var describe : String = "这是麻雀"
    // 延迟赋值的属性
    // lateinit var parent : Bird

    // 动态方法
    open fun fly(){
        println("我会飞")
    }
} // Bird
// kotlin 中的函数是头等公民，函数和类可以平起平坐，不像java一样必须在类中定义方法。这是函数式编程的一大特性。
fun printObjectTest(){
    println("这里是KtObject类")
}
// Cannot access 'accessTest2': it is private in file 。导入一个 private 方法直接标红。
private fun accessTest2(){
    println("测试一下在类外部使用 private 修饰的方法，其他包可以引用吗")
}
/* 在kotlin中，所有的类、方法 都默认使用了 final进行修饰，除非显式使用 open 进行定义，使类可以被继承，方法可以被重写。同时，变量和属性也推荐使用val而不是var进行定义，因为val内部采用final进行修饰。
* 同样的，kotlin，所有的类、方法、属性和全局变量都默认通过 public 进行修饰，除非你显式的使用 private 进行修饰。
*
* */
// 使用 : 指定数据类型，也用于继承 父类 和 实现接口
class Penguin(
    type: String? = "企鹅",
    override var color : MyColorEnum = MyColorEnum.BLACK,
    name :String = "菜鸟",
    gender: GenderEnum? = GenderEnum.Female,
    weight: Double = 1100.0,
    age: Int = 11,

    // 以下参数为子类独有
    egg : Int = 11,
    var childNum : Int = 11 ,
    param: Int = 11
) : Bird() { // Penguin : Bird()
    var param = param  // 可以直接使用构造函数中的参数 param ; 又或者使用kotlin推荐的语法，直接在构造函数中使用 var 或者 val 定义一个变量。例如
    override var gender : GenderEnum? = gender
        // 显示指定 get 和 set ，亦可以采用kotlin的写法，直接不写
        get() = field
        set(value) {field = value}
    override var type: String? = type
    override var name :String = name
    init {
        super.weight = weight  // weight同样没有使用open
        this.age = age // 父类没有使用 open 关键字，尝试对父类属性进行修改
    }
    /* 很奇怪，有4种方式可以对父类中的属性进行赋值。
    * 方式一： 使用 super 关键字  例如: super.weight = weight / 50
    * 方式二： 使用 this 关键字 ， this.age = age
    * 方式三： 在子类的内部通过  override var name :String = name 的方式从构造函数取值。
    * 方式四： 直接在构造函数中赋值。
    * */
    // 定义了 lateinit var 之后，不代表就可以不用赋值。如果你 之后尝试获取这个值时，如果你没赋值，依然会报错(不获取就不会报错)
    //kotlin.UninitializedPropertyAccessException: lateinit property food has not been initialized
    lateinit var food : String
    /** 以下代码报错，egg只是构造函数中的参数，你可以理解成java构造函数中的参数，不是字段，故无法直接在其他函数使用。只可以在构造函数中使用。 */
    fun printEgg(){
        //println(""+egg)
    }
    /** 以下代码则不会报错，因为在默认构造函数中显式使用了  var 或者 val 定义了一个变量。你把鼠标放上去，你就可以看到标注的是“属性”，而不是“参数” */
    fun printChildNum(){
        println("childNum = $childNum")
    }

    fun eat(food : String ){
        this.food = food
    }



    override fun returnType(): String {
        return "企鹅科"
    }


    // 子类应该避免重写父类的非抽象方法
    override fun fly(){
        println("我不会飞")
    }

    //weight 无法自动生成？age 同样; egg是参数。尝试打印两个父类没有用 open修饰的属性
    override fun toString(): String {
        return "Penguin(color=$color, childNum=$childNum, param=$param, gender=$gender, name='$name', food='$food', type='$type'" +
                ", weight=$weight, age=$age"+
                " )"
    }

}  // Penguin


class BirdTest{
    fun test(){
        println("BirdTest")
    }
    /**
     * 测试 set 和 get的一些用法
     */
    @Test
    fun getAndSetTest(){

        val bird  = Penguin()
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
    @Test
    fun printChildNumTest(){
        val bird  = Penguin()
        bird.printChildNum()
    }
    /** 在实例化的时候，由于我们显式定义了初始值，故可以使用无参构造函数。同样，我们可以使用构造函数中的参数实现任意参数组合 */
    @Test
    fun initTest(){
        val bird1  = Penguin()
        val bird2 = Penguin(type = "企鹅",color = MyColorEnum.White,name = "卡哇伊",gender = GenderEnum.Male,weight = 100.0,age = 5,egg = 0,childNum = 0)
        bird1.eat("鱼")
        bird2.eat("虾")
        println("无参构造的企鹅是 $bird1\n有参构造的企鹅是 $bird2")
        // weight=100.0, age=5 .可以看到 都可以对父类的属性进行修改。指向的是父类的属性。而 gender 因为重写了，指向的是自己的属性。同样 color也重写了，指向的是自己的。
        val bird3 = Penguin(type ="跳岩企鹅") // 任意参数构造，需要指定参数的类型
        bird3.eat("虾")
        println("任意参数构造的企鹅是 $bird3")
    }
}


enum class MyColorEnum {
    BLUE,Red,Green,Yellow,BLACK,White
}
enum class GenderEnum {
    Male,Female
}