package com.design.factory.factory;

import com.design.factory.Car;

/**
 * 抽象工厂
 * @author gxyan
 * @Date: 2018/8/11 12:21
 */
public abstract class Driver {
    public abstract Car driverCar(String car);
}
