package org.example.m3concurrency.forkjoin;

public class SingletonMultithreading {
    static void main() {
        SingletonMultithreading instance1 = SingletonMultithreading.getInstance();
    }

    private SingletonMultithreading() {
    }

    private static final class InstanceHolder {
        private static final SingletonMultithreading instance = new SingletonMultithreading();
    }

    public static SingletonMultithreading getInstance(){
        return InstanceHolder.instance;
    }

    private static volatile SingletonMultithreading instance;

    public static SingletonMultithreading getInstance2(){
        if (instance == null){
            synchronized (SingletonMultithreading.class){
                if (instance == null){
                    instance = new SingletonMultithreading();
                }
            }
        }

        return instance;
    }

    void m1() {
    }
}
