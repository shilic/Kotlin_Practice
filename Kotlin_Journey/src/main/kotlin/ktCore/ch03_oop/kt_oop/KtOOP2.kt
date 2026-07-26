package ktCore.ch03_oop.kt_oop


open class GrandpaData (open var value: Int)
open class FatherData(value: Int) : GrandpaData(value)
open class ChildData(value: Int) : FatherData(value)
open class Father {
    open fun func1(data : FatherData) : FatherData {
        return FatherData(data.value + 1)
    }
}
class Child: Father() {
    // 子类覆写的时候，返回值范围可以变得更小
    override fun func1(data : FatherData): ChildData {
        return  ChildData(data.value + 1)
    }
}
class Child2: Father() {
    // 子类覆写的时候，返回值范围不能变得更大， 下边代码报错
//    override fun func1(data : FatherData): GrandpaData {
//        return  GrandpaData(data.value + 1)
//    }
}
class Child3 : Father() {
    // 子类覆写的时候，参数全部都无法修改，否则报错，但是可以重载，以下就是重载
    fun func1(data: GrandpaData): FatherData {
        return FatherData(data.value + 1)
    }
}