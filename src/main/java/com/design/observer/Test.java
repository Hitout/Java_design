package com.design.observer;

public class Test {
    public static void main(String[] args) {
        WechatServer server = new WechatServer();
        Observer userZhang = new User("张三");
        Observer userLi = new User("李四");
        Observer userWang = new User("王五");

        server.registerObserver(userZhang);
        server.registerObserver(userLi);
        server.registerObserver(userWang);

        server.setInfomation("这是观察者模式");

        System.out.println("-------------------------");
        server.removeObserver(userWang);
        server.setInfomation("Java是世界上最好的语言");
    }
}
