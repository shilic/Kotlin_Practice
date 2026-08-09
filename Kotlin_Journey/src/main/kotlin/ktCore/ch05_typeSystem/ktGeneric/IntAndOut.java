package ktCore.ch05_typeSystem.ktGeneric;

public class IntAndOut {
    public static void main(String[] args) {
        Apple[] appleArray = new Apple[10];
        // 允许，因为数组在方法区中保留了元素的类型，故类型确定，不会出现类型安全问题，故可以协变。
        Fruit[] fruitArray = appleArray;
        // 编译通过，运行报错 ArrayStoreException
        fruitArray[0] = new Banana(0.5);
    }
}
