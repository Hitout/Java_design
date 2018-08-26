package com.design.flyweight;

/**
 * 享元模式
 *
 * 优点：
 * 减少系统中对象的个数
 * 能够在不同的环境被共享
 *
 * 缺点：
 * 需要分离出外部状态和内部状态，提高了系统的复杂度
 *
 * @author gxyan
 * @date 2018/8/26 14:11
 */
public class FlyweightPatternDemo {
    public static void main(String[] args) {
        Shape shape1 = FlyweightFactory.getShape("red");
        shape1.draw();

        Shape shape2 = FlyweightFactory.getShape("blue");
        shape2.draw();

        Shape shape3 = FlyweightFactory.getShape("green");
        shape3.draw();

        Shape shape4 = FlyweightFactory.getShape("write");
        shape4.draw();

        Shape shape5 = FlyweightFactory.getShape("red");
        shape5.draw();

        System.out.println("Sum draw " + FlyweightFactory.getSum() + " color of circle.");

    }
}
