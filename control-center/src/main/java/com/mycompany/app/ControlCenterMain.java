package com.mycompany.app;

import com.mycompany.app.model.ControlCenter;

public class ControlCenterMain {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Pass register port, name in parameters");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        String name = args[1];
        ControlCenter center = new ControlCenter(port, name);
        new Thread(center).start();
    }
}