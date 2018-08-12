package com.design.proxy.staticproxy;

import com.design.proxy.IUserDao;
import com.design.proxy.UserDaoImpl;

/**
 * 静态代理
 *
 * 优点：
 * 在符合开闭原则的情况下对目标对象进行功能扩展
 *
 * 缺点：
 * 每一个服务都得创建代理类，工作量太大，不易管理。同时接口一旦发生改变，代理类也得相应修改
 *
 * @author gxyan
 * @Date: 2018/8/12 17:32
 */
public class StaticProxyDemo {
    public static void main(String[] args) {
        // 目标对象
        IUserDao target = new UserDaoImpl();

        // 代理对象
        UserDaoProxy proxy = new UserDaoProxy(target);

        proxy.save();
    }
}
