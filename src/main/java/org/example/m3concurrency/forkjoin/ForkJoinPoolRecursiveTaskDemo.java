package org.example.m3concurrency.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinPoolRecursiveTaskDemo {
    static void main() {
        long start = System.currentTimeMillis();
        double[] a = new double[500_000_000];
        for (int i = 0; i < a.length; i++) {
            a[i] = Math.random();
        }
        long end = System.currentTimeMillis();
        System.out.println("Fill: " + (end - start));

        double limit = 0.3;

        try (ForkJoinPool fjp = ForkJoinPool.commonPool()) {
            SearchGreaterThanLimit task = new SearchGreaterThanLimit(a, limit);

            start = System.currentTimeMillis();
            Integer count = fjp.invoke(task);
            end = System.currentTimeMillis();

            System.out.println("result: " + count);
            System.out.println("time: " + (end - start));
        }

        start = System.currentTimeMillis();
        int count = 0;
        for (double v : a) {
            if (v > limit) {
                count++;
            }
        }
        end = System.currentTimeMillis();

        System.out.println("result: " + count);
        System.out.println("time: " + (end - start));
    }
}

class SearchGreaterThanLimit extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 1000;

    double[] a;
    int start;
    int end;
    double limit;

    public SearchGreaterThanLimit(double[] a, int start, int end, double limit) {
        this.a = a;
        this.start = start;
        this.end = end;
        this.limit = limit;
    }

    public SearchGreaterThanLimit(double[] a, double limit) {
        this(a, 0, a.length, limit);
    }

    @Override
    protected Integer compute() {
        if (end - start < THRESHOLD) {
            int c = 0;

            for (int i = start; i < end; i++) {
                if (a[i] > limit) {
                    c++;
                }
            }

            return c;
        }

        int mid = start + (end - start) / 2;
        SearchGreaterThanLimit left = new SearchGreaterThanLimit(a, start, mid, limit);
        SearchGreaterThanLimit right = new SearchGreaterThanLimit(a, mid, end, limit);

        left.fork();
        Integer rightResult = right.compute();
        Integer leftResult = left.join();

        return rightResult + leftResult;
    }
}
