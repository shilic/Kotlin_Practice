package ktCore.ch03_oop.kt_oop;

public final class OOP3 {
    abstract static class Father {
        // final 明确无法覆写
        final int func1() {
            return 1;
        }
        // 虚方法，子类可选覆写
        int func2() {
            return 2;
        }
        // 必须覆写
        abstract int func3();
    }
    class Child extends Father {
        // 虚方法，子类可选覆写
        @Override
        int func2() {
            return 22;
        }
        @Override
        int func3() {
            return 0;
        }
    }
}
