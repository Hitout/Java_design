package com.design.factory.factory;

import com.design.factory.Shape;

/**
 * 抽象工厂
 * @author gxyan
 * @Date: 2018/8/11 12:21
 */
public interface ShapeFactory {
    Shape getShape(String shape);
}
