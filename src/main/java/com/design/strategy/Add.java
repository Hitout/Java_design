package com.design.strategy;

/**
 * 具体产品
 * @author gxyan
 * @Date: 2018/8/11 17:07
 */
public class Add implements Strategy {
    @Override
    public int doOperation(int num1, int num2) {
        return num1 + num2;
    }
}
