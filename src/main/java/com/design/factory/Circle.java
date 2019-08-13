package com.design.factory;

/**
 * 具体产品
 * @author gxyan
 * @Date: 2018/8/11 10:54
 */
public class Circle implements Shape {
    @Override
    public void draw() {
        System.out.println("Circle draw.");
    }
}
