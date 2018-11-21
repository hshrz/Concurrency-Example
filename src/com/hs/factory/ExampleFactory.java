package com.hs.factory;

import com.hs.RunnableExample;
import com.hs.example.consumer.MessageConsumerExample;
import com.hs.example.worker.WorkerExample;

public class ExampleFactory {

    public static RunnableExample createExample(int index) {
        switch (index) {
            case 0:
                return new WorkerExample();
            case 1:
                return new MessageConsumerExample();
            default:
                return null;
        }
    }
}
