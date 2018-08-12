package com.design.factory.factory;

import com.design.factory.Car;
import com.design.factory.Porsche;

/**
 * 具体工厂
 * @author gxyan
 * @Date: 2018/8/11 12:25
 */
public class PorscheDriver extends Driver {
    @Override
    public Car driverCar(String car) {
        return new Porsche();
    }
}
