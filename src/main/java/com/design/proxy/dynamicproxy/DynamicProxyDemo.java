package com.design.proxy.dynamicproxy;

import com.design.proxy.IUserDao;
import com.design.proxy.UserDaoImpl;

/**
 * 动态代理
 * 减少了对业务接口的依赖，降低了耦合度
 * @author gxyan
 * @Date: 2018/8/12 17:54
 */
public class DynamicProxyDemo {
    public static void main(String[] args) {
        // 目标对象
        IUserDao target = new UserDaoImpl();

        // 代理对象
        IUserDao proxy = (IUserDao) new ProxyHandler(target).getProxyInstance();

        proxy.save();
    }
}
