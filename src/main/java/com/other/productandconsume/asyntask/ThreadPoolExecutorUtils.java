package com.other.productandconsume.asyntask;

import java.util.concurrent.*;

/**
 * 线程池工具类
 * @author gxyan
 * @date 2019/9/30
 */
public class ThreadPoolExecutorUtils {

    private final static Long AWAIT_TERMINATION = 30L;

    /**
     * 创建线程池
     *
     * @param threadNamePrefix 线程名称前缀
     * @param corePoolSize     核心线程数量
     * @param maxPoolSize      最大线程数量
     * @param keepAlive        线程空闲的最大时间
     * @param timeUnit         线程空闲的最大时间单位
     * @param workQueue        工作队列
     * @return
     */
    public static ThreadPoolExecutor newExecutor(String threadNamePrefix, int corePoolSize, int maxPoolSize,
                                                 int keepAlive, TimeUnit timeUnit, BlockingQueue<Runnable> workQueue) {
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAlive, timeUnit,
                workQueue, new ThreadFactory() {
            private int i = 0;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, threadNamePrefix + (++i));
            }
        });
    }


    /**
     * 关闭线程池
     *
     * @param executor
     */
    public static void shutdownExecutor(ExecutorService executor) {
        if (executor == null) {
            return;
        }
        try {
            executor.shutdown();
            if (!executor.awaitTermination(AWAIT_TERMINATION, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
