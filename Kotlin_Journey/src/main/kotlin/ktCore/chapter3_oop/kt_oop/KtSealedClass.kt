package ktCore.chapter3_oop.kt_oop


/**
 * 使用 sealed 定义一个密封类
 */
sealed class KtSealedClass{

}

// 尝试在外部继承一个密封类。同一文件中，可以定义密封类的子类
class SealedData : KtSealedClass() {
    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }

}
