package com.design.factory.factory;

import com.design.factory.Shape;
import com.design.factory.Square;

/**
 * 具体工厂
 * @author gxyan
 * @Date: 2018/8/11 12:24
 */
public class SquareFactory implements ShapeFactory {

    @Override
    public Shape getShape(String shape) {
        return new Square();
    }
}
