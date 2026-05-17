package org.example.m3concurrency.threadpool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalDemo {
    static void main() throws InterruptedException {
        // m1();

        ThreadLocal<SimpleDateFormat> tl = ThreadLocal.withInitial(SimpleDateFormat::new);

        // 4 SimpleDateFormat objects will be created for 4 threads in the pool
        // and then SimpleDateFormat works thread-safe
        try (ExecutorService executorService = Executors.newFixedThreadPool(4)) {
            for (int i = 0; i < 20; i++) {
                executorService.execute(() -> System.out.println(Thread.currentThread().getName() + ": " + tl.get().format(new Date()) + "\t[" + System.identityHashCode(tl.get()) + "]"));
            }
        }

    }

    private static void m1() throws InterruptedException {
        ThreadLocal<Integer> counterThreadLocal = new ThreadLocal<>();
        counterThreadLocal.set(100);

        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(() -> {
                counterThreadLocal.set(finalI);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName() + ":" + counterThreadLocal.get());
            }).start();
        }

        Thread.sleep(2000);

        System.out.println(counterThreadLocal.get());
    }
}
