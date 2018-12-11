package com.hs.example.writer;

import com.hs.RunnableExample;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileWriterExample implements RunnableExample {
    private static final int SPACING_PER_ITEM_MB = 64;
    private static final int NUMBER_OF_THREADS = 4;

    private RandomAccessFile raf;
    private Lock lock;

    @Override
    public void runExample(String[] args) throws Exception {
        writeAll();
    }

    private void writeAll() throws Exception {
        raf = new RandomAccessFile("combined-images.bin", "rw");
        lock = new ReentrantLock();

        ArrayList<FileWriterWorker> workers = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_THREADS; ++i) {
            FileWriterWorker worker = new FileWriterWorker(i);
            workers.add(worker);
            worker.start();
        }

        for (FileWriterWorker worker : workers) {
            worker.join();
        }
        System.out.println("Finished all work.");
        raf.close();
    }

    private class FileWriterWorker extends Thread {
        private final int index;
        private long total;

        private FileWriterWorker(int index) {
            this.index = index;
            this.total = 0;
        }

        @Override
        public void run() {
            try {
                FileInputStream fis = new FileInputStream("image"+index+".jpg");
                for (boolean eof=false; !eof;) {
                    byte[] buffer = new byte[8192 + index*8192];
                    int read = fis.read(buffer, 0, buffer.length);
                    eof = read == -1;
                    if (!eof)
                        write(buffer, read);
                }
                fis.close();
                System.out.println("["+index+"] Finished, with "+ (total/1024/1024) + " mb written!");
            } catch (FileNotFoundException fne) {
                System.out.println("Image file for thread # "+(index+1)+" not found with name: image"+index+".jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private void write(byte[] buffer, int len) throws Exception {
            boolean locked = false;
            try {
                locked = lock.tryLock(1, TimeUnit.MILLISECONDS);
                if (!locked) {
                    System.out.println("[" + index + "] Timed out after 300 ms, unlocking...");
                    return;
                }
                int local = (SPACING_PER_ITEM_MB * 1024 * 1024) * index;
                raf.seek(local + total);
                raf.write(buffer, 0, len);
                System.out.println("[" + index + "] Wrote at " + (local + total) + " with len = " + len);
                total += len;
            } finally {
                if (locked)
                    lock.unlock();
            }
        }
    }
}
