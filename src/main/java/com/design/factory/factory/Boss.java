package com.design.factory.factory;

import com.design.factory.Car;

/**
 * 工厂方法模式
 * 符合开闭原则
 * 只需按照抽象产品角色、抽象工厂角色提供的方法来生成，就可以被客户使用，不必去修改任何已有的代码
 * @author gxyan
 * @Date: 2018/8/11 12:26
 */
public class Boss {
    public static void main(String[] args) {
        // boss今天要坐Benz
        Driver driver = new BenzDriver();
        Car car = driver.driverCar("Benz");
        car.setName("Benz");
        car.driver();
    }
}
