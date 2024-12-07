package com.design.strategy;

/**
 * 策略模式
 * (策略模式与工厂模式从uml图上来说，基本一致。只是强调的封装不同)
 *
 * 优点：
 * 易于扩展
 * 切换自由
 * 容易维护
 *
 * @author gxyan
 * @Date: 2018/8/11 17:16
 */
public class StrategyPatternDemo {
    public static void main(String[] args) {
        Context context = new Context(new Add());
        System.out.println("10 + 5 = " + context.executeStrategy(10, 5));

        context = new Context(new Subtract());
        System.out.println("10 - 5 = " + context.executeStrategy(10, 5));

        context = new Context(new Multiply());
        System.out.println("10 * 5 = " + context.executeStrategy(10, 5));
    }
}
