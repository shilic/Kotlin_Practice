package ktCore.other

import ktCore.chapter3_oop.Bird
import ktCore.chapter3_oop.BirdTest
import ktCore.chapter3_oop.OK
import ktCore.chapter3_oop.printObjectTest


import org.junit.Test

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
        val bird = Bird()
        bird.fly()
        val birdTest = BirdTest()
        birdTest.test()
    }
}
