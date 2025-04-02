package ktCore.ch07_java_and_kotlin.kt_pack

import ktCore.ch07_java_and_kotlin.java_pack.JavaTool
import ktCore.ch07_java_and_kotlin.java_pack.JavaUser
import ktCore.ch07_java_and_kotlin.java_pack.JavaUserDao
import ktCore.ch07_java_and_kotlin.java_pack.OnClickListener
import org.junit.jupiter.api.Test
import javax.swing.text.View

class Kt_Use_Java {
}

class MyButton  {
    lateinit var event : OnClickListener

    fun setOnClickListener(event : OnClickListener){
        this.event = event
    }
    @Test
    fun test(){
        val button : MyButton = MyButton()
        button.setOnClickListener{
                view -> println("Clicked")
        }
    }
}

fun main() {
    // 0. 初始化数据
    val javaUser1 :JavaUser = JavaUser(123456,"蝴蝶")
    val javaUserDao : JavaUserDao = JavaUserDao()
    javaUserDao.addUser(javaUser1)

    // 以下函数有可能返回一个空值，导致空指针异常，因为使用了非空类型来接收，而java代码有可能没有查询到就返回一个空值
    val javaUser : JavaUser = javaUserDao.findUserById(123456)
    // 推荐使用可空类型来处理java的平台类型数据；显示声明可空
    val javaUser2 : JavaUser? = javaUserDao.findUserById(111)
    // 使用可空类型接收的数据，使用上和kotlin一样，需要加? 来引用对象的属性。
    val info1 : String? = javaUser2?.info

    // 1.平台类型的接收(普通java值)。平台类型，有可能为 null ，也有可能不为 null ，kotlin无法确定。以下几种方式都可以，编译器不会有任何报警 。
    // 使用非空类型接收 （不加默认值，还是有可能出现空） （不推荐，可能出现空指针异常）
    val javaUserInfo1 : String = javaUser.info
    // 使用可空类型接收，允许可空 （推荐）
    val javaUserInfo2 : String? = javaUser.info
    // 使用非空类型接收，使用默认值保证绝对非空 （推荐）
    val javaUserInfo3 : String = javaUser.info ?: "default_value"

    // 2.非空类型的接收，使用了 @NotNull 注解(模仿kotlin语法，强制指定变量非空)
    /** 一个 java 的非空类型，如果使用 T? 来接收，那么编译器会警告 */
    val javaUserId1 : Int? = javaUser.id
    /** 对于确认非空的java值，例如使用了 @NotNull 注解，推荐直接使用 非空类型 来接收 */
    val javaUserId2 : Int = javaUser.id
    /** 一个 java 的非空类型，如果使用 T? 来接收，那么编译器会警告 */
    val javaUserName1 : String? = javaUser.name
    /** 对于确认非空的java值，例如使用了 @NotNull 注解，推荐直接使用 非空类型 来接收 */
    val javaUserName2 : String = javaUser.name

    // 3. 可空类型的接收，使用了 @Nullable 注解
    /** 可空类型的接收 ，显式声明可空 ，需要强制使用 T? 来接收 */
    val javaUserNullValue1 : String? = javaUser.nullValue
    /** 可空类型的接收 ， 或者使用 ?: 语法，显式处理可空 */
    val javaUserNullValue2 : String = javaUser.nullValue ?: "default_value"
    /* 可空类型的接收 ， 如果使用了非空类型来接收，并且不显示处理可空性，则报错。下边代码报错 */
    // val javaUserNullValue3 : String = javaUser.nullValue

    // 4.get和set 。 如果java代码正确定义了get和set，那么程序会自动识别
    val info3 = javaUser.info // get
    javaUser.info = "new" // set
    // 以下代码报错，没有设置 get和set
    // val value1 = javaUser.value
    val boolValue = javaUser.isBoolValue // get 这行代码正常运行
    javaUser.isBoolValue = true // set方法


    // 5. 调用java的普通方法
    val javaTool : JavaTool = JavaTool()
    // java方法使用了kotlin的关键字，使用引号将方法名称引用起来
    javaTool.`fun`("hello")
    // 调用普通方法
    javaTool.normalFun("hello")
    // 以下代码都不会报错，对于一个可能返回 null
    val nullValue1 : String = javaTool.nullFun()
    val nullValue2 : String? = javaTool.nullFun()

    // 6. 集合
    val ktList1 : List<JavaUser> = javaTool.users
    //ktList1.add() // 只读，没有add 方法
    val ktList2 = javaTool.users // 自动类型识别，识别成了 MutableList<JavaUser!> 类型
    ktList2.add(JavaUser(10,"梨子"))


    // 7. 异常
    // java的异常， 无论是受检查异常 还是 运行时异常，kotlin都不要求强制捕获
    javaTool.exceptionFun()
    javaTool.runtimeExceptionFun()



}