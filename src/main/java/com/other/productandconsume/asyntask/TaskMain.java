package com.other.productandconsume.asyntask;

import java.util.Scanner;

/**
 * @author gxyan
 * @date 2019/9/30
 */
public class TaskMain {

    public static void main(String[] args) {
        QueueGenerationService service = new QueueGenerationService();

        // 控制台输入
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = scanner.nextLine();
            if ("a".equals(str)) {
                // 生产队列任务
                new Thread(() -> {
                    service.addTask();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else if ("b".equals(str)) {
                // 消费队列任务
                new Thread(() -> {
                    service.processTask();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                break;
            }
        }

        QueueGenerationService.destroy();
    }
}