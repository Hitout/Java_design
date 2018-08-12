package com.design.decorator;

/**
 * 装饰器模式
 * 降低系统的耦合度
 * 代替继承的技术，无需通过继承增加子类就能扩展对象的新功能
 *
 * 优点：
 * 扩展对象功能，比继承灵活
 * 可以对一个对象进行多次装饰
 * 具体构建类和具体装饰类可以独立变化
 *
 * 缺点：
 * 产生很多小对象。大量小对象占据内存，一定程度上影响性能
 * 装饰模式易于出错，调试排查比较麻烦
 * @author gxyan
 * @Date: 2018/8/12 11:29
 */
public class DecoratorPatternDemo {
    public static void main(String[] args) {
        Shape shape = new Circle();
        System.out.println("图案一：");
        shape.draw();

        Shape decoratorShape1 = new RedShapeDecorator(shape);
        System.out.println("\n图案二");
        decoratorShape1.draw();

        Shape decoratorShape2 = new BlueShapeDecorator(shape);
        System.out.println("\n图案三");
        decoratorShape2.draw();

        Shape decoratorShape3 = new GreenShapeDecorator(new Rectangle());
        System.out.println("\n图案四");
        decoratorShape3.draw();
    }
}
