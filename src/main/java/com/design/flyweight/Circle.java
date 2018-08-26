package com.design.flyweight;

/**
 * 具体类
 * @author gxyan
 * @Date: 2018/8/26 14:01
 */
public class Circle implements Shape {
    private String color;

    public Circle(String color) {
        this.color = color;
    }

    @Override
    public void draw() {
        System.out.println("Draw a " + color + " circle");
    }
}
