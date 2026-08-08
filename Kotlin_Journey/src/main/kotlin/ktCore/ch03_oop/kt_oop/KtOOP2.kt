package ktCore.ch03_oop.kt_oop


open class GrandpaData (open var value: Int)
open class FatherData(value: Int) : GrandpaData(value)
open class ChildData(value: Int) : FatherData(value){
    fun childFunc(){ }  // 只有 ChildData 有这个方法
}
open class Father {
    open fun func1(data : FatherData) : FatherData {
        return FatherData(data.value + 1)
    }
}
// ✅ 合法 — 返回收窄（协变返回）
class Child : Father() {
    override fun func1(data: FatherData): ChildData {
        return ChildData(data.value + 1)
    }
}

// ❌ 非法 — 返回变宽
class Child2 : Father() {
    // override fun func1(data: FatherData): GrandpaData {
    //     return GrandpaData(data.value + 1)
    // }
}

// ❌ 非法 — 参数变宽(理论上可以，但JVM拒绝)
class Child3 : Father() {
    // override fun func1(data: GrandpaData): FatherData {
    //     return FatherData(data.value + 1)
    // }
}

// ❌ 非法 — 参数收窄（最危险）
class Child4 : Father() {
    // override fun func1(data: ChildData): FatherData {
    //     data.childFunc()  // 灾难！FatherData 根本没有这个方法
    //     return FatherData(data.value + 1)
    // }
}
