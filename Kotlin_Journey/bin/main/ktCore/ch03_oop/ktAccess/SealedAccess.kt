package ktCore.ch03_oop.ktAccess

/*这个错误信息表明你在使用 Kotlin 的密封类（sealed class）时遇到了一些限制。具体来说，密封类的子类必须在同一文件中定义，而你尝试在一个不同的文件中定义子类。这是 Kotlin 语言的限制，目的是确保密封类的子类是受控的。

错误分析
密封类的子类必须在同一文件中定义：
你定义的密封类 SealedClass 位于 ktCore.chapter3_oop.ktAccess 包中。
你尝试在 ktCore.chapter3_oop.kt_oop 包的文件中定义 SealedClass 的子类，这是不允许的。

构造函数问题：
如果密封类有构造函数，其子类必须调用这个构造函数进行初始化。
* */
// 尝试在外部继承一个密封类 。 以下代码报错
//class SealedAccess : KtSealedClass {
//}

