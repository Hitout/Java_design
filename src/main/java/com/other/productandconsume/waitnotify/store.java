package com.other.productandconsume.waitnotify;

import java.util.LinkedList;

/**
 * @author gxyan
 * @date 2018/10/16 19:06
 */
class Store {

    private final int MAX_PRODUCT = 5;
    private final int MIN_PRODUCT = 0;

    private LinkedList<Object> product = new LinkedList<>();

    /** 生产者 */
    public void produce(String producer)
    {
        synchronized (product) {
            while (this.product.size() >= MAX_PRODUCT) {
                System.out.println("产品已满，【"+ producer +"】暂时不能执行生产任务!,请稍候再生产");
                try {
                    product.wait();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }

            this.product.add(new Object());
            System.out.println("生产者【"+ producer +"】生产第" + product.size() + "个产品\t【现产品量】：" + product.size());
            product.notifyAll();   //通知等待区的消费者可以取出产品了
        }
    }

    /** 消费者 */
    public void consume(String consumer)
    {
        synchronized (product) {
            while (this.product.size() <= MIN_PRODUCT) {
                System.out.println("缺货，【"+ consumer +"】暂时不能执行消费任务！稍候再取");
                try {
                    product.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("\t消费者【"+ consumer +"】取走了第" + product.size() + "个产品\t【现产品量】：" + product.size());
            this.product.remove();
            product.notifyAll();   //通知等待去的生产者可以生产产品了
        }
    }
}
