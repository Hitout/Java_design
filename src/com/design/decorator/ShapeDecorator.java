package com.design.decorator;

/**
 * 抽象装饰类
 * @author gxyan
 * @Date: 2018/8/12 11:23
 */
public abstract class ShapeDecorator implements Shape {
    private Shape decoratorShape;

    public ShapeDecorator(Shape decoratorShape) {
        this.decoratorShape = decoratorShape;
    }

    @Override
    public void draw() {
        decoratorShape.draw();
    }
}
