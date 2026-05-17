package org.example.m3concurrency.synchronization;

import java.util.Random;

public class RaceConditionDemo {
    static void main() {
        final int n = 100;
        final double initialBalance = 100.0;
        Random random = new Random();

        Bank bank = new Bank(n, initialBalance);

        for (int i = 0; i < n; i++) {
            int from = i;
            new Thread(() -> {
                while (true) {
                    bank.transferSynchronized(from, random.nextInt(n), random.nextInt(10000) / 100D);
                }
            }).start();
        }

    }
}

