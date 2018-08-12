package com.design.factory.factory;

import com.design.factory.Benz;
import com.design.factory.Car;

/**
 * 具体工厂
 * @author gxyan
 * @Date: 2018/8/11 12:24
 */
public class BenzDriver extends Driver {
    @Override
    public Car driverCar(String car) {
        return new Benz();
    }
}
