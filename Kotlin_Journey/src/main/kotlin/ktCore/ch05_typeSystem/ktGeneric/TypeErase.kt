package ktCore.ch05_typeSystem.ktGeneric

import org.junit.jupiter.api.Test
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/* 类型推断 */
open class PlateA<T>(val t: T, val clazz: Class<T>) {
    fun getType() {
        println(clazz)
    }
}

open class GenericsToken<T> {
    var type: Type = Any::class.java
    init {
        val superClass = this.javaClass.genericSuperclass
        type = (superClass as ParameterizedType).actualTypeArguments[0]
    }
}

class TypeTest(){
    @Test
    fun typeTest1() {
        /* 获取一个类的类型，通常通过 Apple::class.java 语句。而泛型，因为存在类型擦除，不可以直接获取集合中的泛型。
        * 但是可以通过 list2.javaClass.genericSuperclass 语句获取自身及集合元素的类型
        *  */
        val appleList = ArrayList<Apple>()
        println("appleList.class = ${appleList.javaClass}\n") // 输出：class java.util.ArrayList

        val appleArray = arrayOfNulls<Apple>(3)
        // val anyArray: Array<Any?> = appleArray 不允许


        val applePlateA = PlateA(Apple(1.0), Apple::class.java) // 输出： class ktCore.chapter5_typeSystem.Apple
        println("applePlateA.class = ${applePlateA.getType()}\n") // 输出：applePlateA.class = kotlin.Unit

        // val listType = ArrayList<String>::class.java 不被允许

        val list1 = ArrayList<String>()
        val list2 = object : ArrayList<String>() {}
        println("list1.javaClass = ${list1.javaClass.genericSuperclass}\n") // 输出 java.util.AbstractList<E>
        println("list2.javaClass = ${list2.javaClass.genericSuperclass}\n") // 输出 java.util.ArrayList<java.lang.String>

        val mp = HashMap<String, String>()
        val mpClass = mp.javaClass
        //val gt = object : GenericsToken<Map<String, String>>() {}
        val gt = object : GenericsToken<Map<String, String>>() {}
        println(gt.type) // 输出 ： java.util.Map<java.lang.String, ? extends java.lang.String>

    }
    // 通过内联函数，获取一个类型
    private inline fun <reified T> getType(): Class<T> {
        return T::class.java
    }
    @Test
    fun typeTest2(){
        val clazz = getType<ArrayList<String>>()
        println("clazz = $clazz") // class java.util.ArrayList
    }
}