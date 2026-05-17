package org.example.m3concurrency.synchronization;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Bank {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    private final double[] accounts;

    public Bank(int n, double initialBalance) {
        accounts = new double[n];
        Arrays.fill(accounts, initialBalance);
    }

    public void transfer(int from, int to, double amount) {
        lock.lock();
        try {
            while (accounts[from] < amount) {
                // waits condition.singal(), condition.singalAll()
                // why while?
                // because signalAll() may not be exactly this thread
                // if balance less than amount, this thread waits again
                condition.await();
            }

            accounts[from] -= amount;
            accounts[to] += amount;
            System.out.println(Thread.currentThread().getName() + " transferred " + from + " to " + to + " amount: " + amount);
            System.out.println(totalBalance());

            Thread.sleep(10);

            // signal for waiting threads about transfer money some account
            // if we don't signal, it may occur deadlock.
            // Deadlock - all threads in system wait signal some thread
            condition.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void transferSynchronized(int from, int to, double amount) {
        try {
            synchronized (accounts) {
                while (accounts[from] < amount) {
                    accounts.wait();
                }

                accounts[from] -= amount;
                accounts[to] += amount;
                System.out.println(Thread.currentThread().getName() + " transferred " + from + " to " + to + " amount: " + amount);
                System.out.println(totalBalance());

                Thread.sleep(10);

                accounts.notifyAll();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public double totalBalance() {
        return Arrays.stream(accounts).sum();
    }
}
