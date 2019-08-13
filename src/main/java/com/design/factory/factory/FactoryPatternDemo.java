package com.design.factory.factory;

import com.design.factory.Shape;

/**
 * 工厂方法模式
 * 符合开闭原则
 * 在简单工厂中，创建对象的是另一个类，而在工厂方法中，是由子类来创建对象
 * 只需按照抽象产品角色、抽象工厂角色提供的方法来生成，就可以被客户使用，不必去修改任何已有的代码
 * @author gxyan
 * @Date: 2018/8/11 12:26
 */
public class FactoryPatternDemo {
    public static void main(String[] args) {
        ShapeFactory factory = new CircleFactory();
        Shape circle = factory.getShape("CIRCLE");
        circle.draw();
    }
}
