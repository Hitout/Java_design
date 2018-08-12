package com.design.factory.abstractfactory.superfactory;

import com.design.factory.abstractfactory.color.Blue;
import com.design.factory.abstractfactory.color.Color;
import com.design.factory.abstractfactory.color.Red;
import com.design.factory.abstractfactory.shape.Shape;

/**
 * @author gxyan
 * @Date: 2018/8/11 13:53
 */
public class ColorFactory extends AbstractFactory {
    @Override
    public Color getColor(String color) {
        if(color == null){
            return null;
        }
        if("RED".equalsIgnoreCase(color)){
            return new Red();
        } else if("BLUE".equalsIgnoreCase(color)){
            return new Blue();
        }
        return null;
    }

    @Override
    public Shape getShape(String shape) {
        return null;
    }
}
