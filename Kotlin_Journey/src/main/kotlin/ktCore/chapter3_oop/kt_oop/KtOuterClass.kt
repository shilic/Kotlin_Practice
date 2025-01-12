package ktCore.chapter3_oop.kt_oop

/**
 * 外部内
 */
class KtOuterClass {
    val name = "这是一个外部类"

    fun printN(){
        println("外部类 name = $name")
    }
    // 伴生对象
    companion object {
        // 静态方法
        fun staticMethod() {
            println("这是静态方法")
        }
    }

    /**
     * 内部类的写法，默认是一个 静态内部类。因为内部类和匿名内部类会默认持有外部类的引用，这会导致内存泄漏。
     * 内部类的生命周期比外部类更长，所以就会导致外部类在垃圾回收的时候被持有，导致无法回收。所以kotlin默认内部类的写法实际上是一个静态内部类
     */
    class StaticKotlin{
        var param = 7
        fun printName(){
            // 按理来说，内部类 和 匿名内部类，默认持有外部内的引用，但是这里却报错。 因为在kotlin中，内部类的写法，默认是一个嵌套类（静态内部类）
            //println("name = $name")
        }
    }

    /* 访问外部类的成员：内部类可以直接访问外部类的成员（包括私有成员），而不必通过外部类的实例显式地访问。这使得内部类可以更加自然地与外部类进行交互。
    *  封装和耦合：内部类和外部类之间的这种强耦合关系使得它们可以更紧密地协作，实现复杂的封装和模块化设计。
    *  代码组织：内部类可以将相关的逻辑封装在一起，使得代码更加清晰和模块化。这对于大型项目尤为重要。
    * */
    /**
     * kotlin中的内部类需要用 inner关键字
     */
    inner class InnerKotlin{
        var param = 8
        fun printName(){
            // 内部类可以直接访问外部类的引用
            println("获取外部 name = $name")
            printN() // 使用外部类的方法
        }
    }

}

fun main() {
    KtOuterClass.staticMethod()
    val staticKt = KtOuterClass.StaticKotlin() // 静态内部类通过外部类 . 获取
    val innerKotlin = KtOuterClass().InnerKotlin() // 内部类需要通过外部类的实例才可以获取。
}