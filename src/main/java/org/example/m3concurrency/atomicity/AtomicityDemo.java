package org.example.m3concurrency.atomicity;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicityDemo {
    static void main() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        atomicInteger.incrementAndGet();
        System.out.println(atomicInteger.get());
    }
}
