package com.design.decorator;

/**
 * @author gxyan
 * @Date: 2018/8/12 11:20
 */
public class Rectangle implements Shape {
    @Override
    public void draw() {
        System.out.println("Shape: Rectangle");
    }
}
