package ktCore.chapter3_oop.ktData

import org.junit.jupiter.api.Test

// 你可以在类文件和方法之外定义变量，相当于一个全局变量。这样的变量在整个包中可见，其他包需要 import导入
val OK : Int = 1
val Failure :Int = 0

/**
 * 测试基本数据类型
 */
class KtDataType {
    // 整数类型
    val byteValue: Byte = 127
    val shortValue: Short = 327_67
    val intValue: Int = 214748_3647
    val longValue: Long = 9223372036854775807L

    // 浮点数类型
    val floatValue: Float = 3.14F
    val doubleValue: Double = 3.141592653589793

    // 字符类型
    val charValue: Char = 'A'

    // 布尔类型
    val booleanValue: Boolean = true

    // 字符串类型
    val stringValue: String = "Hello, Kotlin!"

    // 数组类型
    val intArray: IntArray = intArrayOf(1, 2, 3, 4, 5)
    val doubleArray: DoubleArray = doubleArrayOf(1.1, 2.2, 3.3)
    val stringArray: Array<String> = arrayOf("Kotlin", "Java", "Python")
    @Test
    fun printValue(){
        // 打印所有值
        println("Byte Value: $byteValue")
        println("Short Value: $shortValue")
        println("Int Value: $intValue")
        println("Long Value: $longValue")
        println("Float Value: $floatValue")
        println("Double Value: $doubleValue")
        println("Char Value: $charValue")
        println("Boolean Value: $booleanValue")
        println("String Value: $stringValue")
        println("Int Array: ${intArray.joinToString()}")
        println("Double Array: ${doubleArray.joinToString()}")
        println("String Array: ${stringArray.joinToString()}")
    }
    /* 特别注意，kotlin中不区分原始数据类型和包装类。 Int类型可以为空 ,其他几个类型也可以为空。
    * 我们知道，java中 ,int ,byte,short等的初始值是 o ,而 Integer , Byte ,Short等包装类的初始值是 null 。
    * 故kotlin中才需要特别强调每个数据的初始值，你必须确定是 0 或者是 null ，或者是由你自己定义的一个初始值
    * */
    val intValue2 : Int? = null
    val byteValue2: Byte? = null
    val shortValue2: Short? = null
    val longValue2: Long? = null
    val doubleValue2: Double? = null
    /*此处记录我在用java反射时的坑。一个数值类型的数据，它的类型在反射中 可能是是 int.class ，以及 Integer.TYPE ;
    * Integer.class 对应的是  java.lang.Integer。Integer.TYPE 对应的是  int
    *  */
    @Test
    fun integerTest(){
        // 使用 Integer.class（Kotlin 中的表示）
        //val integerClass: Class<Int> = Int::class.java
        val a : Int = 200
        val integerClass = a.javaClass
        println("Int.class: ${integerClass.name}") // 输出：int

        val i :Int? = 18
        val integerClass2 = i?.javaClass
        if (integerClass2 != null) {
            println("Int?.class2: ${integerClass2.name}")// 输出： java.lang.Integer
        }

        // 使用 Integer.TYPE（Kotlin 中的表示）
        val intType: Class<Int> = Integer.TYPE // Integer.TYPE 是java当中的语句
        println("Integer.TYPE: ${intType.name}") // 输出：int

        // 比较两者
        println("Is Integer.class equal to Integer.TYPE? ${integerClass == intType}") // 输出：true
        /* 可见 kotlin 将原始数据类型和 包装类合二为一，避免了装箱和开箱带来的开销，同时还避免了 类型转换带来的错误。 */
    }
}

/**
 * 实验kotlin中几种不同的数组创建方式
 */
private class KtArray(){
    val OK : Int = 1

    //arrayOf() 函数允许你直接创建一个包含指定元素的数组。
    val array1 : Array<Int> = arrayOf(1, 2, 3, 4, 5)

    //Array() 构造函数允许你创建一个指定大小并初始化每个元素的数组。
    val array2 :Array<Int> = Array(5) { i -> i * 2 }  // 创建一个包含 0, 2, 4, 6, 8 的数组

    //Kotlin 还提供了针对基本类型的数组创建函数，如 intArrayOf()、doubleArrayOf() 等，这些函数可以避免装箱操作，提高性能。优先使用基本类型的数组
    //IntArray 但不是 Array的子类。IntArray这些对应java中的 int[] ,char[]等
    val intArray : IntArray= intArrayOf(1, 2, 3, 4, 5)
    val doubleArray: DoubleArray = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0)

    //emptyArray() 函数用于创建一个空的数组。它的类型是 Array<T>
    val emptyArray :Array<Int> = emptyArray<Int>()  //EmptyArray: []

    //arrayOfNulls() 函数用于创建一个指定大小并包含 null 值的数组。
    val nullArray :Array<Int?> = arrayOfNulls<Int>(5)  // 创建一个包含 5 个 null 的数组 //NullArray: [null, null, null, null, null]

    //你可以将一个 List 转换为数组。
    val list = listOf(1, 2, 3, 4, 5)
    val arrayFromList = list.toTypedArray()

    /* 使用数组的构建函数，其他举例 */
    //IntArray 构造函数允许你指定数组的大小，并且可以提供一个初始化器 lambda 表达式来设置每个元素的初始值。
    // 如果你不提供初始化器，所有元素的初始值将默认为 0。 Array则类似
    // 使用 IntArray 构造函数
    val intArray0 = IntArray(5)  // 创建一个包含 5 个 0 的数组 相当于 java中的 int[] intArray = new int[5]
    // 使用 IntArray 构造函数并提供初始化器
    val intArray0_ = IntArray(5) { 0 }  // 创建一个包含 5 个 0 的数组
    // 使用 Array 构造函数（不推荐用于基本类型）
    val array0 = Array(5) { 0 }  // 创建一个包含 5 个 0 的数组，但这是 Int 类型的对象数组
    // 下边的代码报错， Array类型必须在后边接 lambda 表达式给数组元素赋初始值，而 IntArray(5) 则不需要接 lambda 表达式赋初始值，kotlin自动帮你赋值了
    // val array0_2 = Array(6)

    /* 字符串数组的举例 */
    //arrayOf() 函数允许你直接创建一个包含指定字符串元素的数组。
    val stringArray1 :Array<String> = arrayOf("Apple", "Banana", "Cherry")
    //Array() 构造函数允许你创建一个指定大小并初始化每个元素的数组。你可以提供一个 lambda 表达式来设置每个元素的初始值。
    val stringArray2 :Array<String> = Array(3) { index -> "Fruit $index" }  // 创建一个包含 "Fruit 0", "Fruit 1", "Fruit 2" 的数组
    val strArray :Array<String> = Array(3) { " 初始值 " }
    //emptyArray<String>() 函数用于创建一个空的字符串数组。
    val emptyStringArray : Array<String> = emptyArray<String>()
    //arrayOfNulls<String>() 函数用于创建一个指定大小并包含 null 值的字符串数组。
    val nullStringArray :Array<String?> = arrayOfNulls<String>(5)  // 创建一个包含 5 个 null 的数组


    @Test
    fun printStringArray(){
        println("stringArray1 = ${stringArray1.joinToString()}")
    }

    /**
     * 实验kotlin中几种不同的数组创建方式
     */
    @Test
    fun printArray(){
        println("Array1: ${array1.contentToString()}")
        println("Array2: ${array2.joinToString()}")
        println("IntArray: ${intArray.joinToString()}")
        println("EmptyArray: ${emptyArray.contentToString()}") //EmptyArray: []
        println("NullArray: ${nullArray.contentToString()}") //NullArray: [null, null, null, null, null]
        println("Array from List: ${arrayFromList.joinToString()}")
    }
    @Test
    fun print0(){
        println("intArray0: ${intArray0.joinToString()}")
        println("intArray0_: ${intArray0_.joinToString()}")
        println("array0: ${array0.joinToString()}")
    }
} // 数组类型
