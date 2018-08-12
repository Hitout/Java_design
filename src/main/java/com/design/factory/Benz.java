package com.design.factory;

/**
 * 具体产品
 * @author gxyan
 * @Date: 2018/8/11 10:54
 */
public class Benz extends Car {
    @Override
    public void driver() {
        System.out.println(this.getName() + " driving.");
    }
}
