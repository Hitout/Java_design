package com.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用同步器实现线程安全的List
 * @author gxyan
 * @date 2021/2/6
 */
public class ReentrantLockDemo {
    private List<String> list = new ArrayList<>();
    private volatile ReentrantLock lock = new ReentrantLock();

    public void add(String s) {
        lock.lock();
        try {
            list.add(s);
        } finally {
            lock.unlock();
        }
    }

    public void remove(String s) {
        lock.lock();
        try {
            list.add(s);
        } finally {
            lock.unlock();
        }
    }

    public void get(int index) {
        lock.lock();
        try {
            list.get(index);
        } finally {
            lock.unlock();
        }
    }
}
