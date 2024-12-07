package com.design.factory.simple;

import com.design.factory.Shape;

/**
 * 客户端免除了直接创建产品对象的责任，而仅仅负责"消费"产品
 * 例：BeanFactory
 *
 * @author gxyan
 * @Date: 2018/8/11 10:58
 */
public class SimpleFactoryPatternDemo {
    public static void main(String[] args) {
        ShapeFactory shapeFactory = new ShapeFactory();
        Shape circle = shapeFactory.getShape("CIRCLE");
        circle.draw();
    }
}
