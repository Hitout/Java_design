package com.design.decorator;

/**
 * 具体装饰类
 * @author gxyan
 * @Date: 2018/8/12 11:25
 */
public class BlueShapeDecorator extends ShapeDecorator {
    public BlueShapeDecorator(Shape decoratorShape) {
        super(decoratorShape);
    }

    @Override
    public void draw() {
        super.draw();
        setBlueFill();
    }

    private void setBlueFill() {
        System.out.println("Shape color: Blue");
    }
}
