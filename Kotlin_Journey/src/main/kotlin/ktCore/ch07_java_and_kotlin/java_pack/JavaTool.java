package ktCore.ch07_java_and_kotlin.java_pack;

public class JavaTool {
    public static String STATICVALUE = "hello world" ;
    /** 一个普通的方法 ，但是方法名称使用了kotlin的关键字 */
    public void fun (String str){
        System.out.println("关键字重名 fun() 方法，使用引号将方法名称引用起来，打印一个string = "+str);
    }
    /** 一个可能返回 null 值的方法 */
    public String nullFun (){
        return null;
    }

    public void normalFun (String str){
        System.out.println("普通 fun() 方法，打印一个string = "+str);
    }
    /** 静态方法 */
    public static void staticFun(String str){
        System.out.println("staticFun() 方法，打印一个string = "+str);
    }
}
