package com.other.productandconsume.blockingqueue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * 生产者
 * @author gxyan
 * @date 2018/10/16 18:39
 */
public class Producer {

    private final int MAX_PRODUCT = 5;

    private final BlockingQueue<Object> queue;

    public Producer(BlockingQueue <Object> queue) {
        this.queue = queue;
    }

    public void product(String producer) {
        if (queue.size() >= MAX_PRODUCT) {
            System.out.println("产品已满，【" + producer + "】暂时不能执行生产任务!,请稍候再生产");
        }

        try {
            queue.put(new Object());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("生产者【"+ producer +"】生产第" + queue.size() + "个产品\t【现产品量】：" + queue.size());
    }
}
