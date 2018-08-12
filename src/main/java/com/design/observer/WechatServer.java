package com.design.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 被观察者
 * 实现了Observerable接口，对Observerable接口的三个方法进行了具体实现
 * @author gxyan
 */
public class WechatServer implements Observerable {

    private List<Observer> list;
    private String message;

    public WechatServer() {
        list = new ArrayList <Observer>();
    }

    @Override
    public void registerObserver(Observer o) {
        list.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        if (!list.isEmpty()) {
            list.remove(o);
        }
    }

    @Override
    public void notifyObserver() {//遍历
        for (int i=0; i<list.size(); i++){
            Observer observer = list.get(i);
            observer.update(message);
        }
    }

    public void setInfomation(String mess){
        this.message = mess;
        System.out.println("更新消息："+mess);
        notifyObserver();
    }
}
