package ktCore.ch06_lambda_list.ktLambda

import org.junit.jupiter.api.Test

class KtHighLevelFunction {
    @Test
    fun test(){
//Kotlin 提供的高阶函数和作用域函数虽然功能上有一些重叠，但它们各自有不同的用途和行为方式。以下是这些函数的主要区别：
/*
*   函数	    对象引用	    返回值	        使用场景
    with	this	    代码块的结果	    需要在对象上下文中操作并且计算结果（参数传递）
    *
    *
    run	    this	    代码块的结果	    需要在对象上下文中操作并且计算结果
    let	    it	        代码块的结果	    对象转换或者需要对象的操作
    *
    *
    apply	this	    对象本身	        对象的配置和初始化
    also	it	        对象本身	        对象的附加操作，如日志、校验等
* */
/*    -------------------------------------------------------- -作用域函数 ---------------------------------------------*/
/* ++++++++++++++++ 1. apply vs also
* apply 和 also ，在源代码层面都是通过对原始类型进行  "扩展"  来执行的,并且返回原来的对象本身。
* 区别在于， apply 在内部同样通过扩展的方式执行 lambda , 故可以通过this,并省略 this
* 而 also, 则是通过传入当前对象本身来执行函数，故需要 it.方法名 称来执行
* */
        /** apply：
        用途：主要用于对象的配置和初始化。
        返回值：返回调用对象本身。
        上下文对象：在代码块中通过 this 引用。
         */
        val person0 = Person("John", 30).apply {
            name = "Jane"
            age = 25
        }
        /**also：
        用途：主要用于对对象进行一些附加操作，如日志记录或调试。
        返回值：返回调用对象本身。
        上下文对象：在代码块中通过 it 引用。
         */
        val person1 = Person("John", 30).also {
            println("Initializing person: ${it.name}, ${it.age}")
        }
/*        2. with vs run    */
        /** with：
        用途：用于在对象的上下文中执行一系列操作，通常用于不需要返回对象本身的场景。
        返回值：返回代码块的最后一个表达式的值。
        上下文对象：在代码块中通过 this 引用。
         */
        val result0 = with(person0) {
            name = "Jane"
            age = 25
            "Name: $name, Age: $age"
        }
        /** run：
        用途：类似于 with，但它是一个扩展函数，允许对可空对象进行操作。
        返回值：返回代码块的最后一个表达式的值。
        上下文对象：在代码块中通过 this 引用。
         */
        val result1 = person0?.run {
            name = "Jane"
            age = 25
            "Name: $name, Age: $age"
        }
        val result4 = run {
            "直接执行一段代码"
        }

/*       3. let vs run             */
        /**  let：
        用途：用于对对象执行操作并返回结果，通常用于对可空对象进行操作。
        返回值：返回代码块的最后一个表达式的值。
        上下文对象：在代码块中通过 it 引用。
         */
        val result2 = person0?.let {
            it.name = "Jane"
            it.age = 25
            "Name: ${it.name}, Age: ${it.age}"
        }
        /** run：
        用途：用于在对象的上下文中执行操作并返回结果，通常用于对象的初始化和配置。
        返回值：返回代码块的最后一个表达式的值。
        上下文对象：在代码块中通过 this 引用。
         *
         */
        val result3 = person0.run {
            name = "Jane"
            age = 25
            "Name: $name, Age: $age"
        }

/*        4. takeIf vs takeUnless     */
//        takeIf：
//
//        用途：检查对象是否满足某个条件，如果满足则返回对象本身，否则返回 null。
//        返回值：返回对象或 null。
//        示例：

        val youngPerson = person0.takeIf { it.age < 30 }

//        takeUnless：
//
//        用途：检查对象是否不满足某个条件，如果不满足则返回对象本身，否则返回 null。
//        返回值：返回对象或 null。
//        示例：

        val notYoungPerson = person0.takeUnless { it.age < 30 }

/*  -------------------------------------------- 集合操作相关的高阶函数 --------------------------------------------*/
/*        1. map vs filter */
//        map：
//        用途：将集合中的每个元素转换为另一种形式，返回一个新的集合。
//        返回值：返回转换后的集合。
        val numbers0 = listOf(1, 2, 3, 4)
        val squared = numbers0.map { it * it }

//        filter：
//        用途：过滤集合中的元素，返回满足条件的元素组成的新集合。
//        返回值：返回过滤后的集合。
        val numbers1 = listOf(1, 2, 3, 4)
        val evenNumbers = numbers1.filter { it % 2 == 0 }

/*       2. reduce vs fold 。 如果出现异常，reduce 则会抛出异常，fold不会 */
/*在reduce和fold函数中，acc和i是lambda表达式中的参数，它们各自扮演不同的角色。

acc (Accumulator):
含义: 累加器，用于存储累积操作的当前结果。
作用: 在每次迭代中，acc保存了到目前为止的累积结果。例如，在求和操作中，acc会逐渐累加集合中的每个元素。

i (Item):
含义: 当前处理的元素。
作用: 在每次迭代中，i代表集合中当前正在处理的元素。你需要将这个元素与累加器acc进行某种操作，以更新累积结果。
具体来说：

在reduce中:
第一次调用lambda时，acc是集合的第一个元素，i是第二个元素。
后续每次调用，acc是上一次调用的结果，i是集合中的下一个元素。

在fold中:
第一次调用lambda时，acc是初始值（在你的例子中是0），i是集合的第一个元素。
后续每次调用，acc是上一次调用的结果，i是集合中的下一个元素。
*/
//        reduce：
//        用途：对集合中的元素进行累积操作，返回一个单一的结果。
//        返回值：返回累积结果。
        val numbers2 = listOf(1, 2, 3, 4)
        val sum0 = numbers2.reduce { acc, i -> acc + i }

//        fold：
//        用途：与 reduce 类似，但允许指定一个初始值。
//        返回值：返回累积结果。
        val numbers3 = listOf(1, 2, 3, 4)
        val sum1 = numbers3.fold(0) { acc, i -> acc + i }

/*       3. any vs all     */
//        any：
//        用途：检查集合中是否有至少一个元素满足给定的条件。
//        返回值：返回布尔值。
        val numbers4 = listOf(1, 2, 3, 4)
        val hasEven = numbers4.any { it % 2 == 0 }
//        all：
//        用途：检查集合中的所有元素是否都满足给定的条件。
//        返回值：返回布尔值。
        val numbers5 = listOf(2, 4, 6, 8)
        val allEven = numbers5.all { it % 2 == 0 }

    }
}
val result5 = run {
    "直接执行一段代码"
}
class Person(var name: String, var age: Int) {
//    var name: String = name
//    var age : Int = age
}
