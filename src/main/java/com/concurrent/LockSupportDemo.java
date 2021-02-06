package com.concurrent;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport.park()：调用线程若未关联许可证会被阻塞
 * LockSupport.unpark()：获取许可证并唤醒当前挂起线程
 *
 * @author gxyan
 * @date 2021/2/6
 */
public class LockSupportDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("Thread park：调用park挂起线程");
            LockSupport.park();
            System.out.println("------End park!------");
        });
        thread.start();

        // 休眠1s
        Thread.sleep(1000);

        System.out.println("Thread unpark：调用unpark唤醒线程");
        LockSupport.unpark(thread);
    }
}
