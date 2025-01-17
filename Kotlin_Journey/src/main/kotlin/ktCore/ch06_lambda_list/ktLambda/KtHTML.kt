package ktCore.ch06_lambda_list.ktLambda

import org.junit.jupiter.api.Test

class KtHTML {
    fun body(){
        println("body")
    }
}
fun ktHtml(init : KtHTML.() -> Unit): KtHTML{
    val html = KtHTML()
    html.init()
    return html
}
class KtHtmlTest(){
    @Test
    fun test1(){
        ktHtml {
            body()
        }
    }
}