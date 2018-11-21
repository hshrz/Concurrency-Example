package com.hs.example.consumer;

public class Message<T> {
    private final T value;

    protected Message(T v) {
        this.value = v;
    }

    public T value() {
        return value;
    }
}
