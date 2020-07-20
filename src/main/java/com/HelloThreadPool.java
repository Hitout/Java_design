package com;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @author gxyan
 */
public class HelloThreadPool {
    // public static void main(String[] args) {
    //     ThreadPool threadPool = new ThreadPool();
    //     threadPool.init();
    //     threadPool.submitThread();
    //     threadPool.submitThread();
    //     threadPool.submitThread();
    //     threadPool.destroy();
    // }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int result = 0;
                for (int i = 0; i < 100; i++) {
                    Thread.sleep(10);
                    result += i;
                }
                return result;
            }
        });

        Thread computeThread = new Thread(futureTask);
        computeThread.start();

        Thread otherThread = new Thread(() -> {
            System.out.println("other task is running...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        otherThread.start();
        System.out.println(futureTask.get());
    }
}

class ThreadPool {

    private ExecutorService executorService;

    public void init() {
        executorService = new ThreadPoolExecutor(2, 2, 0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(30), r -> {
            System.out.println("ThreadFactory is Run.");
            return new Thread(r);
        }, new ThreadPoolExecutor.AbortPolicy());
        // executorService = new ThreadPoolExecutor(2, 2, 0, TimeUnit.SECONDS,
        //         new ArrayBlockingQueue<>(30), new ThreadFactoryBuilder().setNameFormat("pool-%d").build(), new ThreadPoolExecutor.AbortPolicy());
    }

    public void submitThread() {
        Future<String> submit = executorService.submit(() -> Thread.currentThread().getName() + "\tThis is Callable Task");

        try {
            System.out.println(submit.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        executorService.shutdown();
    }
}
