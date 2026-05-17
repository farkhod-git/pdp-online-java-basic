package org.example.m3concurrency.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ExecutorServicesDemo {
    static void main() throws InterruptedException {
        // ExecutorService

        Runnable[] tasks = IntStream.range(0, 3)
                .mapToObj(i -> (Runnable) () -> {
                    for (int j = 0; j < 2; j++) {
                        System.out.println(Thread.currentThread().getName() + " :: " + i);
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).toArray(Runnable[]::new);

        // There is one thread in the pool
        // This is single thread pool
        // Tasks do consecutively
        try (ExecutorService executorService = Executors.newSingleThreadExecutor()) {
            for (Runnable task : tasks) {
                executorService.execute(task);
            }
        }

        Thread.sleep(1000);
        System.out.println("FIXED THREAD POOL");

        // cores in the CPU
        int processors = Runtime.getRuntime().availableProcessors();

        // Some threads with fixed count do tasks
        try (ExecutorService executorService = Executors.newFixedThreadPool(processors)) {
            for (Runnable task : tasks) {
                executorService.execute(task);
            }
        }


        // If tasks increase, threads in the pool increases too
        // If threads wait without work, pool will kill them
        try (ExecutorService executorService = Executors.newCachedThreadPool()) {
            for (Runnable task : tasks) {
                executorService.execute(task);
            }
        }

        System.out.println("\nScheduled thread pool\n");

        // execute the tasks after scheduled time
        // now after 4 seconds task will do
        try (ScheduledExecutorService executorService = Executors.newScheduledThreadPool(processors)) {
            for (Runnable task : tasks) {
                System.out.println("A");
                executorService.schedule(task, 1, TimeUnit.SECONDS);
            }

            System.out.println("Scheduled delayed thread\n");

            // fixed rate doesn't wait to finish task, runs again after period seconds
            executorService.scheduleAtFixedRate(tasks[0], 2, 2, TimeUnit.SECONDS);

//             fixed delay waits to finish task
//             then runs task again
            executorService.scheduleWithFixedDelay(tasks[1], 2, 2, TimeUnit.SECONDS);

//            executorService.shutdown();
//            boolean b = executorService.awaitTermination(5, TimeUnit.SECONDS);
//            System.out.println(b);
//            executorService.shutdownNow();

            // safe termination
            executorService.shutdown();
            if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }

        }


    }
}
