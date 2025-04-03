package ktCore.ch07_java_and_kotlin.java_pack;

import ktCore.ch07_java_and_kotlin.kt_pack.KtToolKt;
import org.junit.jupiter.api.Test;

public class Java_Use_Kt {

    public static void main(String[] args) {
        test();
    }

    public static void test(){
        // 文件 KtTool.kt 自动编译成KtToolKt工具类，以使用其中的顶层函数
        KtToolKt.funA();

        // 以下代码报错，不可以直接使用 kotlin 的语法来操作
        // "fake".helloWorld();
        KtToolKt.helloWorld("fake");
    }

}
