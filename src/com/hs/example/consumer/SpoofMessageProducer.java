package com.hs.example.consumer;

import com.hs.util.Utils;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class SpoofMessageProducer implements Runnable {

    private static final int MAX_NUMBER_OF_RUNS = 10;

    private final Queue<Message> messageQueue;
    private final Condition signaller;
    private final Lock lock;

    protected SpoofMessageProducer(Queue<Message> queue, Lock lock, Condition signaller) {
        this.messageQueue = queue;
        this.lock = lock;
        this.signaller = signaller;
    }

    @Override
    public void run() {
        int numRuns = MAX_NUMBER_OF_RUNS;
        while (numRuns > 0) {
            lock.lock();

            // Enqueue as many messages as needed to feed all the consumer threads,
            // whilst we still hold the lock
            for (int i = 0; i < MessageConsumerExample.NUMBER_OF_THREADS; ++i)
                enqueueSpoofMessage();

            signaller.signalAll();

            lock.unlock();
            try {
                Thread.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            --numRuns;
        }
    }

    private void enqueueSpoofMessage() {
        Message<Integer> message = new Message<Integer>(Utils.generateRandomNumber(4000));
        messageQueue.add(message);
        System.out.println("[ Producer ] Generated spoof client request, message = " + message.value());
    }
}
