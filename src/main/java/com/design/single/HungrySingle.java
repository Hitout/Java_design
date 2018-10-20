package com.design.single;

/**
 * 饿汉式
 * 线程安全
 *
 * @author gxyan
 * @date 2018/10/20 10:52
 */
public class HungrySingle {
    private static HungrySingle ins = new HungrySingle();

    /** 私有化构造方法 */
    private HungrySingle() {
        System.out.println("HungrySingle constructor");
    }

    public static HungrySingle instance() {
        return ins;
    }
}
