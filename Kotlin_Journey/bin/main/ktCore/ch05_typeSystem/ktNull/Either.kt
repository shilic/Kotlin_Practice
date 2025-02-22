package ktCore.ch05_typeSystem.ktNull

/**
 * Created by prefert on 2019/5/13.
 */
// Either 表达式
sealed class Either<A, B>() {
    class Left<A, B>(val value: A) : Either<A, B>()
    class Right<A, B>(val value: B) : Either<A, B>()
}

fun getDegreeOfMyopiaKt(seat: Seat?): Either<Error, Double> {
    // Either 表达式 ?: ;当左侧的数值为空时，则选择右侧的默认值  Either.Left<Error, Double>(Error("-1"))
    return seat?.student?.glasses?.let {
        Either.Right<Error, Double>(it.degreeOfMyopia)
    }
        ?:
        Either.Left<Error, Double>(Error("-1"))
    // obj.let{  }  语法; 将obj作为参数传入后边的函数中，默认参数名称是it，let的返回值就是后边的函数返回值
}