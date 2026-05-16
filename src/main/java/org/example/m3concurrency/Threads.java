package org.example.m3concurrency;

public class Threads {
    static void main() {
        // java.lang.Thread class (since 1.0)
        // java.lang.Runnable interface (since 1.0)

        // result of m1 method ignored
        int _ = m1();

        try {
            Thread.sleep(100);
        } catch (Exception _) {
            // exception catch ignored
        }
    }

    static int m1() {
        return 0;
    }
}
