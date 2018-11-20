package com.hs.example.worker;

import com.hs.RunnableExample;
import com.hs.util.Utils;

import java.util.ArrayList;

public class WorkerExample implements RunnableExample {

    public static final int NUMBERS_TO_GENERATE = 100000;
    public static final int NUMBER_OF_THREADS = 4;

    private ArrayList<WorkerThread> workers;
    protected int[] numbers;

    public WorkerExample() {
        workers = new ArrayList<>();
    }

    @Override
    public void runExample(String[] args) throws Exception {
        numbers = Utils.generateRandomNumbers(NUMBERS_TO_GENERATE);

        Utils.printShort(numbers, NUMBERS_TO_GENERATE);

        dispatchWorkerThreads(NUMBER_OF_THREADS, NUMBERS_TO_GENERATE);

        waitForWorkers();
    }

    private void dispatchWorkerThreads(int i, int size){
        workers.clear();
        int range = size/i;
        for (int j=0;j<i;++j) {
            WorkerThread worker = new WorkerThread(j+1, j * range, (j+1) * range);
            workers.add(worker);
            worker.start();
        }
        System.out.println("Created "+range+" lots of "+i+" workers...");
    }

    private synchronized void waitForWorkers() throws Exception {
        int size = workers.size();
        for (Thread thread : workers) {
            --size;
            thread.join();
            System.out.println("Joined thread, remaining workers = "+ size);
        }
        // Final sort, pretty much destroys the point of multithreaded sorting but this is
        // purely an example of dispatching multiple threads and then joining them at the end
        new WorkerThread(NUMBER_OF_THREADS+1, 0, numbers.length).run();
        synchronized (this) {
            Utils.printShort(numbers, 5);
        }
    }

    protected class WorkerThread extends Thread {

        private final int index, start, end;

        public WorkerThread(int index, int start, int end) {
            this.index=index;
            this.start = start;
            this.end = end;
        }

        // Simple Sort
        @Override
        public void run() {
            // TODO: solve for proper sorting
            synchronized (numbers) {
                System.out.print("[Start] Worker thread #" + index + ": ");
                Utils.printShort(numbers, 4, start, end);
            }
            for (int i = start; i < end; i++) {
                for (int j = start; j < end; j++) {
                    if (numbers[i] < numbers[j]) {
                        int temp = numbers[i];
                        numbers[i] = numbers[j];
                        numbers[j] = temp;
                    }
                }
            }
            synchronized (numbers) {
                System.out.print("[End] Worker thread #" +index+": ");
                Utils.printShort(numbers, 4, start, end);
            }
        }
    }
}
