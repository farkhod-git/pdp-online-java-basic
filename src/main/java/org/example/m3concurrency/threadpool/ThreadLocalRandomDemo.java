package org.example.m3concurrency.threadpool;

import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

public class ThreadLocalRandomDemo {
    static void main() {
        // get object of Random class for current thread
        ThreadLocalRandom random = ThreadLocalRandom.current();
        System.out.println(random.nextInt(100));
    }
}
