package com.design.proxy;

/**
 * 接口实现
 * @author gxyan
 * @Date: 2018/8/12 17:28
 */
public class UserDaoImpl implements IUserDao {
    @Override
    public void save() {
        System.out.println("-----保存数据-----");
    }
}
