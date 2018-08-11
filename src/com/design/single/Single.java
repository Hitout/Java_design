package com.design.single;

public class Single {
    private static Single ins;

    //私有化构造方法
    private Single() {
        System.out.println("constructor");
    }

    public static Single instance() {
        if (ins == null) {
            //实例化
            ins = new Single();
        }
        return ins;
    }
}
