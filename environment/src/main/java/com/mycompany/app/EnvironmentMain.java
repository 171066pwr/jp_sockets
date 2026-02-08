package com.mycompany.app;

import com.mycompany.app.model.Environment;
import com.mycompany.app.model.RainGenerator;

public class EnvironmentMain {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Pass register port, name and [optional] update period, average rainfall in parameter");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        String name = args[1];
        long period = args.length < 3 ? 5000: Long.parseLong(args[2]);
        int average = args.length < 4 ? 13: Integer.parseInt(args[3]);

        Environment environment = new Environment(port, name);
        RainGenerator rainGenerator = new RainGenerator(environment, average, period);
        new Thread(rainGenerator).start();
    }
}