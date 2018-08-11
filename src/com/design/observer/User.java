package com.design.observer;

/**
 * 观察者
 * 实现了update方法
 */
public class User implements Observer {

    private String name;
    private String message;

    public User(String name) {
        this.name = name;
    }

    @Override
    public void update(String massage) {
        this.message = massage;
        read();
    }

    private void read() {
        System.out.println(name + "收到消息：" + message);
    }
}
