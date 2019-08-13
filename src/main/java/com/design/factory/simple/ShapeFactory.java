package com.design.factory.simple;

import com.design.factory.Circle;
import com.design.factory.Shape;
import com.design.factory.Square;

/**
 * 简单工厂(不属于23种设计模式)
 * (违背开闭原则)
 * 将产品的生产（实例化）放入工厂中
 * @author gxyan
 * @Date: 2018/8/11 10:49
 */
public class ShapeFactory {
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
