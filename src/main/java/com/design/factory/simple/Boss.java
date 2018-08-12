package com.design.factory.simple;

import com.design.factory.Car;

/**
 * 客户端免除了直接创建产品对象的责任，而仅仅负责"消费"产品
 * @author gxyan
 * @Date: 2018/8/11 10:58
 */
public class Boss {
    public static void main(String[] args) {
        // boss今天要坐Porsche
        Car car = Driver.driverCar("Porsche");
        car.setName("Porsche");
        car.driver();
    }
}
