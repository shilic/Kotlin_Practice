package ktCore.ch03_oop.kt_oop


private class EqualsTest(
    var name: String = "",
    var age: Int = 0
)


fun main() {
    val data1 = EqualsTest("123", 123)
    val data2 = EqualsTest("123", 123)
    /* 即便值相同，但是因为没有 覆写 equals ，导致都不相等
    如果不重写 equals，Kotlin 类继承自 Any 的默认实现，使用的是引用相等性（即 ===）
    * */
    println("data1.equals(data2) = ${data1.equals(data2)}")
    println("data1 == data2 = ${data1 == data2}")
}