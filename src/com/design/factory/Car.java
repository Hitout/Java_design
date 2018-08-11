package com.design.factory;

/**
 * 抽象产品
 * 符合开闭原则的--对扩展开放、对修改关闭
 * @author gxyan
 * @Date: 2018/8/11 10:50
 */
public abstract class Car {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void driver();
}
