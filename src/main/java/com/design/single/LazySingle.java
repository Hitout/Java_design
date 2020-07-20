package com.design.single;

/**
 * 懒汉式
 * 线程不安全，双检锁改进后线程安全
 *
 * @author gxyan
 */
public class LazySingle {
    /** 使用volatile禁止重排序 */
    private static volatile LazySingle ins;

    /** 私有化构造方法 */
    private LazySingle() {
        System.out.println("LazySingle constructor");
    }

    public static LazySingle instance() {
        // 双重检查
        if (ins == null) {
            synchronized (LazySingle.class) {
                if (ins == null) {
                    ins = new LazySingle();
                }
            }
        }
        return ins;
    }
}
