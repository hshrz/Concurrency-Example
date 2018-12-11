package com.hs.factory;

import com.hs.RunnableExample;
import com.hs.example.consumer.MessageConsumerExample;
import com.hs.example.worker.WorkerExample;
import com.hs.example.writer.FileWriterExample;

public class ExampleFactory {

    public static RunnableExample createExample(int index) {
        switch (index) {
            case 0:
                return new WorkerExample();
            case 1:
                return new MessageConsumerExample();
            case 2:
                return new FileWriterExample();
            default:
                return null;
        }
    }
}
