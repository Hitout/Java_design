package com.design.proxy.cglibproxy;

import com.design.proxy.IUserDao;
import com.design.proxy.UserDaoImpl;

/**
 * CGLIB代理
 * 对于没有接口的类实现动态代理
 *
 * 原理：
 * 通过字节码技术为一个类创建子类，并在子类中采用方法拦截的技术拦截所有父类方法的调用，顺势织入横切逻辑
 * 因为采用的是继承，所以不能对final修饰的类进行代理
 *
 * JDK动态代理与CGLIB动态代理均是实现Spring AOP的基础
 *  · 如果加入容器的目标对象有实现接口，用JDK代理
 *  · 如果目标对象没有实现接口，用CGLIB代理
 *
 * @author gxyan
 * @Date: 2018/8/12 20:06
 */
public class CglibProxyDemo {
    public static void main(String[] args) {
        // 目标对象
        IUserDao target = new UserDaoImpl();

        // 代理对象
        UserDaoImpl proxy = (UserDaoImpl) new ProxyFactory(target).getInstance();

        proxy.save();
    }
}
