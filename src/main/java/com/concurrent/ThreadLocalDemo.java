package com.concurrent;

/**
 * @author gxyan
 * @date 2021/2/5
 */
public class ThreadLocalDemo {
    static ThreadLocal<String> localVariable = new ThreadLocal<>();

    public static void main(String[] args) {
        Thread threadOne = new Thread(() -> {
            localVariable.set("threadOne local variable");
            print("threadOne");
        });

        Thread threadTwo = new Thread(() -> {
            localVariable.set("threadTwo local variable");
            print("threadTwo");
        });

        threadOne.start();
        threadTwo.start();
    }

    static void print(String str) {
        System.out.println(str + ": " + localVariable.get());
    }
}
