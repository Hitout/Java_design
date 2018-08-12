package com.design.factory.abstractfactory;

import com.design.factory.abstractfactory.superfactory.AbstractFactory;
import com.design.factory.abstractfactory.superfactory.ColorFactory;
import com.design.factory.abstractfactory.superfactory.ShapeFactory;

/**
 * @author gxyan
 * @Date: 2018/8/11 13:56
 */
public class FactoryProducer {
    public static AbstractFactory getFactory(String choice) {
        if("SHAPE".equalsIgnoreCase(choice)){
            return new ShapeFactory();
        } else if("COLOR".equalsIgnoreCase(choice)){
            return new ColorFactory();
        }
        return null;
    }
}
