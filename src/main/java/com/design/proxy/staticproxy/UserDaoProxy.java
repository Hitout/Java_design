package com.design.proxy.staticproxy;

import com.design.proxy.IUserDao;

/**
 * 静态代理
 * @author gxyan
 * @Date: 2018/8/12 17:30
 */
public class UserDaoProxy implements IUserDao {
    private IUserDao target;

    public UserDaoProxy(IUserDao target) {
        this.target = target;
    }

    @Override
    public void save() {
        System.out.println("Begin...");
        target.save();
        System.out.println("End...");
    }
}
