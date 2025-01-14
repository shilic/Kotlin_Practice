package ktCore.ch06_lambda_list.ktCollection

import org.junit.jupiter.api.Test

class KtMap {
    /*在 Kotlin 中，你可以使用 mapOf 函数来创建一个不可变的映射，或者使用 mutableMapOf 函数来创建一个可变的映射。以下是一些示例：*/

    //不可变映射：
    private val immutableMap = mapOf("key1" to "value1", "key2" to "value2")

    //可变映射：
    val mutableMap = mutableMapOf("key1" to "value1", "key2" to "value2")

    /* 你也可以通过构造函数来创建映射：*/
    //不可变映射：
    private val immutableMap2 = mapOf("key1" to "value1", "key2" to "value2")

    //可变映射：
    private val mutableMap2 = HashMap<String, String>()

    @Test
    fun mapTest(){
        mutableMap2["key1"] = "value1" // 可以采用 [] 赋值语句
        mutableMap2.put("key2", "value2")

        /* 请注意，mapOf 创建的映射是不可变的，这意味着你不能添加、删除或修改其中的键值对。而 mutableMapOf 或 HashMap 创建的映射是可变的，你可以对其进行修改。 */
        //访问映射中的值：

        //你可以通过键来访问映射中的值：
        val valueKey1 = immutableMap2["key1"]

        //检查键是否存在：
        //你可以使用 in 运算符来检查某个键是否存在于映射中：
        if ("key1" in immutableMap) {
            println("Key exists")
        }

        //遍历映射：
        //你可以使用 for 循环来遍历映射的键值对：

        for ((key, value) in mutableMap2) {
            println("Key: $key, Value: $value")
        }

    }
}