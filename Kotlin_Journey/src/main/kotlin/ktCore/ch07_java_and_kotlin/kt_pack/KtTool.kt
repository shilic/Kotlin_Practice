// 文件名称是：KtTool.kt
package ktCore.ch07_java_and_kotlin.kt_pack

// 一个顶层函数
fun funA(): String {
    //...
    return "aaa"
}
// 一个扩展函数
fun String.helloWorld(){
    println("$this hello World")
}