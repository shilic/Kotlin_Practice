package ktCore.ch03_oop.ktData

import org.junit.jupiter.api.Test


class KtLoop {
    val languageList = listOf<String>("java","kotlin","C++","C#","python","javascript")
    val fruitArray :Array<String> = arrayOf("Apple", "Banana", "Cherry")

    /* List 是一个有序的集合，可以包含重复元素。 */
    // 创建一个不可变的 List
    val immutableList : List<String> = listOf("apple", "banana", "cherry")
    // 创建一个可变的 List
    val mutableList :  MutableList<String> = mutableListOf("apple", "banana", "cherry");
    // 添加元素到可变 List
    init{ mutableList.add("date") }
    // 访问元素
    val firstElement = immutableList[0]  // "apple"

    /* Set 是一个无序的集合，不允许重复元素。*/
    // 创建一个不可变的 Set
    val immutableSet : Set<String> = setOf("apple", "banana", "cherry")
    // 创建一个可变的 Set
    val mutableSet :  MutableSet<String> = mutableSetOf("apple", "banana", "cherry")
    // 添加元素到可变 Set
    init { mutableSet.add("date") }
    // 检查元素是否存在
    val containsBanana = immutableSet.contains("banana")  // true

    /* Map 是一个键值对的集合，键是唯一的。 */
    // 创建一个不可变的 Map
    val immutableMap : Map<String ,Int> = mapOf("apple" to 1, "banana" to 2, "cherry" to 3)
    // 创建一个可变的 Map
    val mutableMap : MutableMap<String ,Int> = mutableMapOf("apple" to 1, "banana" to 2, "cherry" to 3)
    // 添加元素到可变 Map
    init { mutableMap["date"] = 4 }
    // 访问元素
    val bananaValue = immutableMap["banana"]  // 2

    /* 你可以创建空集合，然后根据需要添加元素。*/
    // 创建一个空的不可变 List
    val emptyList : List<String> = emptyList<String>()
    // 创建一个空的不可变 Set
    val emptySet : Set<String> = emptySet<String>()
    // 创建一个空的不可变 Map
    val emptyMap : Map<String ,Int> = emptyMap<String, Int>()

    // 创建一个空的可变集合
    val emptyMutableList : MutableList<String> = mutableListOf()
    // 创建一个空的 ArrayList
    val emptyArrayList : ArrayList<String> = arrayListOf()
    // 使用构造函数创建一个空的 ArrayList
    val emptyArrayList2 : ArrayList<String> = ArrayList()
    // 要创建一个空的可变 MutableMap，可以使用 mutableMapOf() 函数：
    val emptyMutableMap: MutableMap<String, String> = mutableMapOf()
    // 你也可以通过构造函数来创建一个空的 HashMap，它是 MutableMap 的一个具体实现：
    val emptyHashMap: HashMap<String, String> = HashMap()


    /* 5. 使用 arrayListOf 和 hashSetOf  . Kotlin 还提供了一些快捷方式来创建基于特定实现的集合。 */
    // 创建一个 ArrayList
    val arrayList = arrayListOf("apple", "banana", "cherry")
    // 创建一个 HashSet
    val hashSet = hashSetOf("apple", "banana", "cherry")

}
class KtLoopTest {
    @Test
    fun loopTest(){
        val data = KtLoop()
        val list =  data.languageList
        val fruitArray = data.fruitArray
        /* for 循环 ： 对索引值进行循环 ，类似于 java 中的 for ( int i = 0; i<length ; i++) */
        val str1 : StringBuilder = StringBuilder().append("1.until循环：language : ")
        for (i in 0 until list.size){ // until 左闭右开
            str1.append("[$i]=${list[i]}, ")
        }
        println(str1.toString())

        val str2 : StringBuilder = StringBuilder().append("2.范围表达式循环：language : ")
        for (i : Int in 0..5 ){ // 不包含6 ，也可以写作 0..(list.size-1)
            str2.append("[$i]=${list[i]}, ")
        }
        println(str2.toString())

        val str3 : StringBuilder = StringBuilder().append("3.indices循环：language : ")
        for (i in list.indices){ // list.indices 表示循环 list 的索引值
            str3.append("[$i]=${list[i]}, ")
        }
        println("2. list.indices = ${list.indices.joinToString()}") // list.indices = 0, 1, 2, 3, 4, 5
        println(str3.toString())

        // 可以同时循环“索引”和“值”
        val str4 : StringBuilder = StringBuilder().append("4.循环“索引”和“值”：language : ")
        for ((index, value) in list.withIndex()) {
            str4.append("[$index]=${value}, ")
        }
        println(str4.toString())

        /* for 循环 ： 对 元素 进行循环，类似java中的 forEach */
        val str5 : StringBuilder = StringBuilder().append("5.循环元素：fruitArray : ")
        for (item : String in fruitArray){
            str5.append("$item , ")
        }
        println(str5.toString())

        val str6 : StringBuilder = StringBuilder().append("6.forEach 循环：fruitArray : ")
        fruitArray.forEach { item ->
            str6.append("$item , ")
        }
        println(str6.toString())

        val str7 : StringBuilder = StringBuilder().append("7.downTo 循环：fruitArray : ")
        for (i in (fruitArray.size -1)  downTo 0){
            str7.append("${fruitArray[i]} , ")
        }
        println(str7.toString())

    }
    @Test
    fun labelTest(){
        outerLoop@
        for (i in 1..3) {
            for (j in 1..3) {
                println("i = $i, j = $j")
                if (i == 2 && j == 2) {
                    break@outerLoop  // 跳出外层循环
                }
            }
        }
    }

    // in 关键字还能用于判断 元素是否在集合中
    @Test
    fun inTest(){
        val list =   KtLoop().languageList
        val g = "Go" in list
        val j = "java" in list
        println("java在列表中吗？$j , Go在列表中吗？$g")
    }

}
