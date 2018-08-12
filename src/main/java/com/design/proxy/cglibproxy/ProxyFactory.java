package com.design.proxy.cglibproxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB子类代理工厂
 * @author gxyan
 * @Date: 2018/8/12 19:36
 */
public class ProxyFactory implements MethodInterceptor {
    private Object target;

    public ProxyFactory(Object target) {
        this.target = target;
    }

    public Object getInstance() {
        // 1.工具类
        Enhancer en = new Enhancer();
        // 2.设置父类
        en.setSuperclass(target.getClass());
        // 3.设置回调函数
        en.setCallback(this);
        // 4.创建子类(代理对象)
        return en.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("Begin...");
        Object value = methodProxy.invoke(target, objects);
        System.out.println("End...");
        return value;
    }
}
