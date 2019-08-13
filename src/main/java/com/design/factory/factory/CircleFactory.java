package com.design.factory.factory;

import com.design.factory.Circle;
import com.design.factory.Shape;

/**
 * 具体工厂
 * @author gxyan
 * @Date: 2018/8/11 12:25
 */
public class CircleFactory implements ShapeFactory {
    @Override
    public Shape getShape(String shape) {
        return new Circle();
    }
}
