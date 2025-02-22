package internal

class KtAccessObject {
    val param = "我是internal包下，public 修饰的参数"
    fun printParam(){
        println(param)
    }
}
internal class KtAccessObject2{
    val param = "我是internal包下，internal修饰的参数"
    fun printParam(){
        println(param)
    }
}