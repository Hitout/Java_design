package com.design.factory.simple;

import com.design.factory.Benz;
import com.design.factory.Car;
import com.design.factory.Porsche;

/**
 * 简单工厂(静态工厂方法)(不属于23种设计模式)
 * (违背开闭原则)
 * @author gxyan
 * @Date: 2018/8/11 10:49
 */
public class Driver {
    public static Car driverCar(String car) {
        Car c = null;
        if ("Benz".equals(car)) {
            c = new Benz();
        } else if ("Porsche".equals(car)) {
            c = new Porsche();
        }
        return c;
    }
}
