package ktCore.ch05_typeSystem.ktGeneric


/** Comparator 源码如下
 * public expect fun interface Comparator<T> {
       public fun compare(a: T, b: T): Int
   }
sortWith 源码如下
.sortWith(comparator: Comparator<in T>): Unit
 使用了 in 关键字，使 (in T )成为了 T 的父类
 */
val doubleComparator = Comparator<Double> {
    d1, d2 -> d1.compareTo(d2)
}

val numberComparator = Comparator<Number> {
    n1, n2 -> n1.toDouble().compareTo(n2.toDouble())
}

fun copy(dest: Array<Double?>, src: Array<Double>) {
    if (dest.size < src.size) {
        throw IndexOutOfBoundsException()
    } else {
        src.forEachIndexed {index, _ -> dest[index] = src[index]}
    }
}

fun <T> copy(dest: Array<T?>, src: Array<T>) {
    if (dest.size < src.size) {
        throw IndexOutOfBoundsException()
    } else {
        src.forEachIndexed {index, _ -> dest[index] = src[index]}
    }
}

/**
 * 泛型的逆变 in 相当于java中的 method< ? super T> , ?是T的父类 。
 * 同样的，"in T" 相当于 T的父类。
 */
fun <T> copyIn(dest: Array<in T>, src: Array<T>) {
    if (dest.size < src.size) {
        throw IndexOutOfBoundsException()
    } else {
        src.forEachIndexed {index, _ -> dest[index] = src[index]}
    }
}

/**
 * 泛型的协变 out 相当于java中的 method< ? extends T> , ?是T的子类。
 * 也就是说 "out T" 是 T 的子类;   顾名思义，就是从T中读出，那么也就只能是 ? extends T ,也就是 T的子类。只读，读出来是T的子类，这样才可以保证类型安全
 * 可以作为返回值,如 List 就采用了 out 关键字。有 get 方法 返回一个 泛型。协变只可以用于返回值，不可以用于参数。
 * 如果是参数则需要加上@UnsafeVariance E注解用于指示在特定位置，尽管类型是协变的，但使用它是安全的，因为不会引起类型不安全性。
 */
fun <T> copyOut(dest: Array<T>, src: Array<out T>) {
    if (dest.size < src.size) {
        throw IndexOutOfBoundsException()
    } else {
        src.forEachIndexed {index, _ -> dest[index] = src[index]}
    }
}
// 两个函数的写法其实是一样的，都是将 src 拷贝到 dest 中。src 中的数据必须是 dest 中的子类。

fun main(args: Array<String>) {

    val doubleList = mutableListOf(2.0,3.0)
    doubleList.sortWith(doubleComparator)

    doubleList.sortWith(numberComparator)

    val intList = mutableListOf(1,2)
    intList.sortWith(numberComparator)

    var dest = arrayOfNulls<Double>(3)
    val src = arrayOf(1.0,2.0,3.0)


    copy(dest, src)

    var destDouble = arrayOfNulls<Double>(3)
    val srcDouble = arrayOf(1.0,2.0,3.0)
    copy(destDouble, srcDouble)

    var destInt = arrayOfNulls<Int>(3)
    val srcInt = arrayOf(1,2,3)
    copy(destInt, srcInt)

    copyIn(dest, src)
    copyOut(dest, src)

}