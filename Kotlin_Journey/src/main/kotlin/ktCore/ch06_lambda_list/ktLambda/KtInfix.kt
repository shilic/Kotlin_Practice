package ktCore.ch06_lambda_list.ktLambda

import org.junit.jupiter.api.Test

class KtInfix {

}
class KtPerson1{
    infix fun called(name : String) {
        println("我的名字是 $name")
    }
}
class KtPerson1Test{
    val p = KtPerson1()

    /**
     * 中缀表达式
     */
    @Test
    fun test1() {
        p called "卡哇伊" //我的名字是 卡哇伊
    }
}