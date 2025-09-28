package com.hq.test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class QuartzTest {
	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        ExecutorService pool = Executors.newWorkStealingPool();
        Future<String> future = pool.submit(() -> {
            System.out.println("我要睡觉了");
            TimeUnit.SECONDS.sleep(5);
            System.out.println("我醒了");
            return "";
        });
        try {
            future.get(20, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
            System.out.println("超时了小伙子");
            System.out.println("执行线程的状态：" + future.isDone());
            System.out.println("我要杀死你");
            future.cancel(true);
            System.out.println("再看执行线程的状态：" + future.isDone());
        }
        future.get();
        System.out.println("到主线程了");
        pool.shutdown();
        pool.shutdownNow();
        pool = null;
}
}
