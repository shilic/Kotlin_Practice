package ktCore.ch05_typeSystem.ktNull

/**
 * Created by prefert on 2019/5/13.
 */

open class Human
data class Teacher(val name: String) : Human()

fun getStu(): Student? {
    return Student(null)
}
// 自动类型转换，子类变父类可直接变
fun getTeacher(): Human? {
//    仅示例，实际存在可空的情况
//    return Teacher("jilen")
    return null
}

class Kot {
    private val stu: Student? = getStu() as? Student

    fun dealStuA() {
        if (stu != null) {
            print(stu.glasses)
        }
    }

    fun dealStu() {
        stu?.let { print(it.glasses) }
    }
}

fun main(args: Array<String>) {

    val stu: Any = Student(Glasses(189.00))
    // Any 是所有类的父类。 相当于java中的object , 使用is进行类型转换的判断，相当于 instanceof
    if (stu is Student) {
        //stu as Student // 可手动过声明转换，但是这里kotlin自动检测了
        println(stu.glasses)
    }
    // 使用as进行强制类型转换，相当于java中的 强制类型转换。而使用 as? 如果转换对象为空，则不会报错，而是同样返回空
    val teacher = getTeacher() as? Teacher  // 将 Human? 类型转换 为 Teacher 。父类转子类需要使用 as 关键字

//    不可直接调用 name ，可能存在为空的情况
//    teacher.name
    if (teacher !== null) {
        print(teacher.name)
    }

//    JAVA
//    Object stu = Student(Glasses(189.00))
//    if(stu instanceof Student) System.out.println(((Student)stu).glasses

}