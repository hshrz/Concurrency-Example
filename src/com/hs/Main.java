package com.hs;

import com.hs.factory.ExampleFactory;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("A number argument is required, use a range from 0-4");
            return;
        }
        int exampleNumber = Math.min(Math.max(Integer.parseInt(args[0]), 0), 4);
        RunnableExample example = ExampleFactory.createExample(exampleNumber);
        if (example == null) {
            return;
        }
        example.runExample(args);
    }

}
