package com.other.productandconsume.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 通过阻塞队列实现
 * BlockingQueue是一个阻塞队列，它的存取可以保证只有一个线程在进行
 * 生产者在内存满的时候进行等待，并且唤醒消费者队列
 * 消费者在饥饿状态下等待并唤醒生产者进行生产
 *
 * @author gxyan
 * @date 2018/10/16 18:49
 */
public class Main {
    public static void main(String[] args) {
        BlockingQueue<Object> queue = new LinkedBlockingQueue <>(100);
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        for (int i = 0; i < 6; i++) {
            int finalI = i;
            new Thread(() -> producer.product(String.format("生产者%d", finalI))).start();
        }

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> consumer.consume(String.format("消费者%d", finalI))).start();
        }
    }
}
