package com.design.decorator;

/**
 * 具体装饰类
 * @author gxyan
 * @Date: 2018/8/12 11:25
 */
public class RedShapeDecorator extends ShapeDecorator {
    public RedShapeDecorator(Shape decoratorShape) {
        super(decoratorShape);
    }

    @Override
    public void draw() {
        super.draw();
        setRedFill();
    }

    private void setRedFill() {
        System.out.println("Shape color: Red");
    }
}
