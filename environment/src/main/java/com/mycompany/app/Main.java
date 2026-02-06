package com.mycompany.app;

import com.mycompany.app.model.Environment;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Pass register port in parameter");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);

        Environment environment = new Environment(port,4000, 5000);
        new Thread(environment).start();
    }
}