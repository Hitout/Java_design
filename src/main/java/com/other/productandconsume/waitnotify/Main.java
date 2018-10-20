package com.other.productandconsume.waitnotify;

/**
 * @author gxyan
 * @date 2018/10/16 19:22
 */
public class Main {
    public static void main(String[] args) {
        Store store = new Store();

        for (int i = 0; i < 6; i++) {
            int finalI = i;
            new Thread(() -> store.produce(String.format("生产者%d", finalI))).start();
        }

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> store.consume(String.format("消费者%d", finalI))).start();
        }
    }
}
