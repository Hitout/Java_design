package com.design.factory.abstractfactory.superfactory;

import com.design.factory.Shape;
import com.design.factory.abstractfactory.color.Color;

/**
 * 抽象工厂
 * 超级工厂(其他工厂的工厂)
 * @author gxyan
 * @Date: 2018/8/11 13:50
 */
public abstract class AbstractFactory {
    public abstract Color getColor(String color);
    public abstract Shape getShape(String shape);
}
