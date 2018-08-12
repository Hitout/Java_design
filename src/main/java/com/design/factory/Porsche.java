package com.design.factory;

/**
 * 具体产品
 * @author gxyan
 * @Date: 2018/8/11 10:58
 */
public class Porsche extends Car {
    @Override
    public void driver() {
        System.out.println(this.getName() + " driving.");
    }
}
