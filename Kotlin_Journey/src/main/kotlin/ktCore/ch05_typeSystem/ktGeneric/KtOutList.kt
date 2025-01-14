package ktCore.ch05_typeSystem.ktGeneric

import org.junit.jupiter.api.Test


// kotlin 中的集合相比于 java中的集合，支持协变。
class KtOutList {

    val stringList : List<String> = ArrayList<String>()
    val anyList : List<Any> = stringList
    // 程序不会报错 ， 可以看到在 kotlin 源码中，public interface List<out E> : Collection<E> 。
    // 程序加了一个out关键字，表示泛型类是协变的。 但同时也是 不可变的。out 关键字的意思表示读出，也就是说只读的意思

    val strList2 : ArrayList<String> = ArrayList<String>()
    // 下边代码报错。 类型不匹配。提示我将 接收的数据类型改为 ArrayList<String>
    // val anyList2 : ArrayList<Any> = strList2
    /* 我们可以来看一下源码。  kotlin 中的 List 和 ArrayList 的区别。 ArrayList直接就引用了 java中的 ArrayList .
    而 java 中的集合是不支持协变的，故上边一行代码报错。而 kotlin 中的 List 支持协变，是因为kotlin 做了优化。
    另外，java中可以使用 List< ? extends Object> list 的方式让集合支持协变。

    @SinceKotlin("1.1") public actual typealias ArrayList<E> = java.util.ArrayList<E>
    @SinceKotlin("1.1") public actual typealias LinkedHashMap<K, V> = java.util.LinkedHashMap<K, V>
    @SinceKotlin("1.1") public actual typealias HashMap<K, V> = java.util.HashMap<K, V>
    @SinceKotlin("1.1") public actual typealias LinkedHashSet<E> = java.util.LinkedHashSet<E>
    @SinceKotlin("1.1") public actual typealias HashSet<E> = java.util.HashSet<E>
    * */


    @Test
    fun test1(){
        //anyList.add("kotlin") // 程序报错，因为 List 支持协变，但代价是 没有了 add 方法
    }

}