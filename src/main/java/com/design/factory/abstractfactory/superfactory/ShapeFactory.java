package com.design.factory.abstractfactory.superfactory;

import com.design.factory.Circle;
import com.design.factory.Shape;
import com.design.factory.Square;
import com.design.factory.abstractfactory.color.Color;

/**
 * @author gxyan
 * @Date: 2018/8/11 13:53
 */
public class ShapeFactory extends AbstractFactory {
    @Override
    public Color getColor(String color) {
        return null;
    }

    @Override
    public Shape getShape(String shape) {
        if(shape == null){
            return null;
        }
        if("CIRCLE".equalsIgnoreCase(shape)){
            return new Circle();
        } else if("SQUARE".equalsIgnoreCase(shape)){
            return new Square();
        }
        return null;
    }
}
