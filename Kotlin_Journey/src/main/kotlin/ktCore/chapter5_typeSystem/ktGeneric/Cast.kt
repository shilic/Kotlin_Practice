package ktCore.chapter5_typeSystem.ktGeneric

/**
 * Created by prefert on 2019/5/13.
 */
// 封装了两个
fun <T> castA(original: Any): T? = original as? T

// 通过 reified，你可以绕过类型擦除的限制，直接在运行时获取泛型类型的具体信息。
// reified 只能与 inline 函数一起使用，因为它的实现依赖于编译器在调用点展开代码，从而保留类型信息。
inline fun <reified T> cast(original: Any): T? = original as? T

fun <T> printType(value: T) {
    // 无法直接获取 T 的具体类型
    //println(T::class.java) // 编译错误：Cannot use 'T' as reified type parameter
}
inline fun <reified T> isType(value: Any): Boolean {
    return value is T
}
fun main(args: Array<String>) {
    // java.lang.Long cannot be cast to java.lang.String
//    val ansA = castA<String>(140163L)
    val ans = cast<String>(140163L)
    println(ans)

    val value = "Hello"
    println("'$value' isType<String>? ${isType<String>(value)}") // true
    println("'$value' isType<Int>? ${isType<Int>(value)}")    // false
}

/*在 Kotlin 中，inline 是一个函数修饰符，用于内联函数。内联函数在编译时会被展开到调用它们的地方，而不是像普通函数那样在运行时进行调用。这可以减少函数调用的开销，提高性能，特别是在函数体很小的情况下。
使用 inline 的一些好处包括：
性能优化：通过内联，函数调用被展开到调用处，避免了函数调用的 overhead，从而提高执行效率。
减少代码量：对于小函数，内联可以减少代码的大小，因为函数体被直接插入到调用点。
支持局部返回：内联函数可以使用 return 语句直接返回到调用它们的函数，而不仅仅是从内联函数本身返回。
lambda 表达式优化：当内联函数接受 lambda 表达式作为参数时，Kotlin 编译器可以进行一些优化，比如尾递归优化和避免捕获不必要的变量。
* */

/*
* 在 Kotlin 中，reified 是与 inline 函数结合使用的关键字，用于支持具体化类型参数（reified type parameters）。
* 通常，Kotlin 的泛型类型参数是在编译时被擦除的（类型擦除，type erasure），这意味着在运行时你无法直接访问泛型类型。
* 然而，通过 reified，你可以绕过类型擦除的限制，直接在运行时获取泛型类型的具体信息。

为什么需要 reified？
在 Java 和 Kotlin 中，泛型类型参数在运行时会被擦除（例如，List<String> 在运行时会变成 List）。这种类型擦除机制是为了兼容旧版本的 JVM，但它限制了一些操作，比如：

fun <T> printType(value: T) {
    // 无法直接获取 T 的具体类型
    println(T::class.java) // 编译错误：Cannot use 'T' as reified type parameter
}
通过 reified，你可以解决这个问题，允许在运行时获取泛型类型的具体信息。

如何使用 reified？
reified 只能与 inline 函数一起使用，因为它的实现依赖于编译器在调用点展开代码，从而保留类型信息。

例如：
inline fun <reified T> isType(value: Any): Boolean {
    return value is T
}
fun main() {
    val value = "Hello"
    println(isType<String>(value)) // true
    println(isType<Int>(value))    // false
}
在这个例子中：

inline 函数允许编译器在调用点展开代码。
reified 使得泛型类型 T 在运行时可以被访问。
* */