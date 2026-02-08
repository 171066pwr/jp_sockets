package com.mycompany.app;

import com.mycompany.app.model.ControlCenter;
import com.mycompany.app.model.LoadBalancer;

public class ControlCenterMain {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Pass register port, name in parameters");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        String name = args[1];
        ControlCenter center = new ControlCenter(port, name);
        LoadBalancer balancer = new LoadBalancer(center);
        new Thread(balancer).start();
    }
}