package com.hs.example.consumer;

import com.hs.util.Utils;

import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MessageConsumer implements Runnable {

    private static int count = 0;

    private final int consumerIndex;
    private final Queue<Message> messageQueue;
    private final Condition signaller;
    private final Lock lock;

    protected MessageConsumer(Queue<Message> queue, Lock lock, Condition signaller) {
        this.messageQueue = queue;
        this.lock = lock;
        this.signaller = signaller;

        consumerIndex = ++count;
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            try {
                waitForSignal();
                Message<Integer> message = messageQueue.poll();

                // No more messages in queue, exit
                if (message == null) {
                    break;
                }
                System.out.println("[ Consumer " + consumerIndex + " ] Got a message request from the spoof client, message = " + message.value());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            // Simulate processing delay
            try {
                Thread.sleep(Utils.generateRandomNumber(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitForSignal() throws InterruptedException {
        while (messageQueue.peek() == null) {
            boolean timedout = !signaller.await(5000, TimeUnit.MILLISECONDS);
            if (timedout) {
                System.out.println("[ Consumer " + consumerIndex + "] finished processing messages...");
                break;
            }
        }
    }
}
