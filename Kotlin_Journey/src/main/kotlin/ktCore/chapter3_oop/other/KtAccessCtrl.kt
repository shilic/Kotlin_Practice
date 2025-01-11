package ktCore.chapter3_oop.other

import internal.KtAccessObject

import ktCore.chapter3_oop.*
import ktCore.chapter3_oop.ktData.OK

// 以下代码报错，编译器提示我将模块设置为public，而不是internal。
//import internal.KtAccessObject2


import org.junit.jupiter.api.Test

// 导入一个 private 方法直接标红
//import ktCore.chapter3_oop.accessTest2

/**
 * 访问控制，测试类
 */
class KtAccessCtrl {

    /**
     * 测试外部访问，导入外部方法和外部的变量
     */
    @Test
    fun accessTest() {
        printObjectTest() // 外部 public 的方法
        println("ok的值 = $OK") // 外部 public 的变量
        // 导入一个 private 方法直接标红
        //accessTest2()
    }

    /**
     * 如你可见，KtObject 文件中定义了多个类，他们也都是 public 的类，kotlin中默认是 public 的。外部可以任意访问
     */
    @Test
    fun accessTest2() {
        val bird = Penguin()
        bird.fly()
        val birdTest = BirdTest()
        birdTest.test()
    }
    /**
     * 跨模块访问测试
     */
    @Test
    fun accessTest3(){
        //可以在build.gradle文件中，通过 implementation project(':Internal_Module') // 依赖 其他模块
        //再 import internal.KtAccessObject 添加其他模块的类
        val obj1 : KtAccessObject = KtAccessObject() // KtAccessObject模块是public的，其他类可见
        obj1.printParam()
        // 以下代码报错，编译器提示我将模块设置为public，而不是internal。
        //val obj2 :KtAccessObject2 = KtAccessObject2()
    }
}
