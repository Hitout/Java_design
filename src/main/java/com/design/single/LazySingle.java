package com.design.single;

/**
 * 懒汉式
 * 线程不安全
 *
 * @author gxyan
 */
public class LazySingle {
    private static LazySingle ins;

    /** 私有化构造方法 */
    private LazySingle() {
        System.out.println("LazySingle constructor");
    }

    public static LazySingle instance() {
        if (ins == null) {
            //实例化
            ins = new LazySingle();
        }
        return ins;
    }
}
