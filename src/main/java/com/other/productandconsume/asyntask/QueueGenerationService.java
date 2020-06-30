package com.other.productandconsume.asyntask;

import com.other.productandconsume.ThreadPoolExecutorUtils;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 队列服务类
 * @author gxyan
 * @date 2019/9/30
 */
public class QueueGenerationService {

    private static final BlockingQueue<QueueTaskHandler> QUEUE = new LinkedBlockingQueue<>(3);

    private static final ThreadPoolExecutor EXECUTOR = ThreadPoolExecutorUtils.newExecutor(
            "Queue-Service"
            , 1, 2, 3, TimeUnit.MINUTES,
            new LinkedBlockingDeque<>(100)
    );

    public void processTask() {
        Future<?> submit = EXECUTOR.submit(() -> {
            if (QUEUE.size() <= 0) {
                System.out.println("队列为空，请先生产再消费。");
            }
            QueueTaskHandler take = QUEUE.take();
            return take.processData();
        });
        try {
            System.out.println("消费：" + submit.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void addTask() {
        if (QUEUE.size() > 2) {
            System.out.println("队列已满，请消费后再生产。");
        }
        try {
            int next = new Random().nextInt(300);
            QUEUE.put(new ServiceHandlerImpl(String.valueOf(next)));
            System.out.println("生产：" + next);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void destroy() {
        ThreadPoolExecutorUtils.shutdownExecutor(EXECUTOR);
    }
}
