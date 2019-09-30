package com.other.productandconsume.blockingqueue;

import java.util.concurrent.BlockingQueue;

/**
 * 消费者
 * @author gxyan
 * @date 2018/10/16 18:47
 */
public class Consumer {

    private final int MIN_PRODUCT = 0;

    private final BlockingQueue<Object> queue;

    public Consumer(BlockingQueue <Object> queue) {
        this.queue = queue;
    }

    public void consume(String consumer) {
        if (queue.size() <= MIN_PRODUCT) {
            System.out.println("缺货，【"+ consumer +"】暂时不能执行消费任务！稍候再取");
        }

        try {
            queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\t消费者【"+ consumer +"】取走了第" + queue.size() + "个产品\t【现产品量】：" + queue.size());
    }
}
