package ktCore.ch07_java_and_kotlin.kt_pack

data class KtUser(val id : Int,val name : String){
    /** 同样的，kt 中一个可以为 null 的变量 。 */
    lateinit var info: String
    /** 同样的，kt 中一个可以为 null 的变量 。 */
    var de: String? = null
}
