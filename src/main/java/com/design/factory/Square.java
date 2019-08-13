package com.design.factory;

/**
 * 具体产品
 * @author gxyan
 * @Date: 2018/8/11 10:58
 */
public class Square implements Shape {
    @Override
    public void draw() {
        System.out.println("Square draw.");
    }
}
