package ktCore.ch05_typeSystem.ktAny

/**
 * Created by prefert on 2019/5/13.
 */
// 使用 is 判断类型
fun typeCheckA(obj: Any) {
    if (obj is String) {
        println("'$obj' .length = ${obj.length}")
    }
    if (obj !is String) { // 等同于 !(obj is String) print("Not a String")
        println("'$obj' Not a String")
    } else {
        println("'$obj' .length = ${obj.length}")
    }
}

// 使用when 表达式配合 is 进行类型检查
fun typeCheckB(obj: Any) {
    when (obj) {
        is String -> println("'$obj' .length = ${obj.length}")
        else      -> println("'$obj' Not a String")
    }
}


fun main(args: Array<String>) {
    typeCheckA("hello kotlin!")
    typeCheckB(1024)
    typeCheckB("Prefer.t")
}