package com.mycompany.app;

import com.mycompany.app.model.RiverSection;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Pass register port and name in parameter");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        String name = args[1];
        int baseFlow = args.length < 3 ? 10: Integer.parseInt(args[1]);
        long delay = args.length < 4 ? 5000: Long.parseLong(args[2]);

        RiverSection section = new RiverSection(port, name, 4000, baseFlow, delay);
        new Thread(section).start();
    }
}