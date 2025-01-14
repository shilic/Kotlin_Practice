package ktCore.ch05_typeSystem.ktNull

/**
 * Created by prefert on 2019/5/13.
 */
// ？ 可空类型
data class Seat(val student: Student?)
data class Student(val glasses: Glasses?)
data class Glasses(val degreeOfMyopia: Double)
