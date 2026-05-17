package org.example.m3concurrency.threadpool;

import java.util.List;
import java.util.concurrent.*;

public class CallableFutureDemo {
    static void main() {
        Callable<String> task1 = () -> {
            Thread.sleep(2000);
            return "task1";
        };

        Callable<String> task2 = () -> {
            Thread.sleep(2000);
            return "task2";
        };

        try (ExecutorService executorService = Executors.newFixedThreadPool(5)) {
            // f1 takes 2 seconds another thread
            Future<String> f1 = executorService.submit(task1);

            for (int i = 0; i < 10; i++) {
                if (f1.isDone()) {
                    break;
                }

                Thread.sleep(50);
            }

            if (!f1.isDone()) {
                // f1.cancel(true);
            }

            // f2 tasks 2 seconds another thread
            Future<String> f2 = executorService.submit(task2);

            // f1 and f2 works parallel, so both takes 2 seconds
            String r1 = f1.get();
            String r2 = f2.get();

            System.out.println(r1 + " and " + r2);


            System.out.println("invokeAll()");
            List<Future<String>> futures = executorService.invokeAll(List.of(task1, task2));
            for (Future<String> future : futures) {
                System.out.println(future.get());
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    String getAPI(String url) {
        Callable<String> apiResp = () -> {
            Thread.sleep(2000);
            return "result of " + url;
        };

        try (ExecutorService executorService = Executors.newSingleThreadExecutor()) {
            Future<String> f = executorService.submit(apiResp);
            return f.get(3, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            return "TIME OUT";
        }
    }
}
