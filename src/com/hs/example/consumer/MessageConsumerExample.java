package com.hs.example.consumer;

import com.hs.RunnableExample;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MessageConsumerExample implements RunnableExample {

    static final int NUMBER_OF_THREADS = 4;

    private final LinkedList<Message> queue;
    private final ReentrantLock lock;
    private final Condition signaller;

    private SpoofMessageProducer producer;

    public MessageConsumerExample() {
        queue = new LinkedList<>();
        lock = new ReentrantLock();
        signaller = lock.newCondition();
    }

    @Override
    public void runExample(String[] args) throws Exception {
        startProducerThread();

        ArrayList<Thread> consumerThreads = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_THREADS; ++i) {
            Thread consumerThread = spawnConsumerThread();
            consumerThread.start();
            consumerThreads.add(consumerThread);
        }
        while (!consumerThreads.isEmpty()) {
            Thread thread = consumerThreads.remove(consumerThreads.size() - 1);
            thread.join();
        }
    }

    private Thread spawnConsumerThread() throws InterruptedException {
        MessageConsumer consumer = new MessageConsumer(queue, lock, signaller);
        Thread consumerThread = new Thread(consumer);
        return consumerThread;
    }

    private void startProducerThread() {
        producer = new SpoofMessageProducer(queue, lock, signaller);
        new Thread(producer).start();
    }
}
