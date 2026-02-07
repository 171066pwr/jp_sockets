package com.mycompany.app;

import com.mycompany.app.model.Environment;

public class EnvironmentMain {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Pass register port in parameter");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        long period = args.length < 2 ? 5000: Long.parseLong(args[1]);

        Environment environment = new Environment(port, period);
        new Thread(environment).start();
    }
}