package ktCore.chapter3_oop;

public interface Flyer {
    int getSpeed();
    //int speed; // 定义字段报错
    int height = 1000; // 但是可以定义有默认值的字段
    void kind();
    void fly();
    public static final class DefaultImpls{
        public static void fly(Flyer $this){
            String var1 = "我能飞";
            System.out.println(var1);
        }
    }
}
