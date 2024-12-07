package com.jvm;

import com.design.observer.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gxyan
 * @date 2021/3/1
 *
 * JVM设置：-Xms10M -Xmx10M -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\jvm.dump
 * 可通过dump文件，分析类创建实例查找原因
 */
public class OOMTest {
    public static List<Object> list = new ArrayList<>();

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (true) {
            // 内存溢出
            list.add(new User("name" + i++));
            new User("name" + j--);
        }
    }
}
