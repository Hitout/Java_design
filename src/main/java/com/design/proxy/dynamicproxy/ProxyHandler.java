package com.design.proxy.dynamicproxy;

import java.lang.reflect.Proxy;

/**
 * 动态处理器
 * 不需要实现接口
 * 利用JDK的API,动态的在内存中构建代理对象
 * @author gxyan
 * @Date: 2018/8/12 17:49
 */
public class ProxyHandler {
    private Object target;

    public ProxyHandler(Object target) {
        this.target = target;
    }

    public Object getProxyInstance() {
        /*
         * static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces,InvocationHandler h)
         *  · ClassLoader loader：目标对象的类加载器
         *  · Class<?>[] interfaces：目标对象实现的接口的类型
         *  · InvocationHandler h：事件处理，执行目标对象的方法时，会触发事件处理器的方法，把当前执行目标对象的方法作为参数传入
         */
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    System.out.println("Begin...");
                    Object value = method.invoke(target, args);
                    System.out.println("End...");
                    return value;
                }
        );
    }
}
