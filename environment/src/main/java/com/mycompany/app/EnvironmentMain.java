package com.mycompany.app;

import com.mycompany.app.gui.EnvironmentGui;
import com.mycompany.app.model.Environment;
import com.mycompany.app.model.RainGenerator;

public class EnvironmentMain {
    public static void main(String[] args) {
        EnvironmentGui gui;
        if (args.length < 2) {
            System.err.println("Pass register port, name and [optional] update period, average rainfall in parameter");
            gui = new EnvironmentGui();
        } else {
            int port = Integer.parseInt(args[0]);
            String name = args[1];
            long period = args.length < 3 ? 5000 : Long.parseLong(args[2]);
            int average = args.length < 4 ? 13 : Integer.parseInt(args[3]);
            Environment environment = new Environment(port, name);
            RainGenerator rainGenerator = new RainGenerator(environment, average, period);
            gui = new EnvironmentGui(environment, rainGenerator);
        }
        gui.setSize(600, 400);
        //gui.pack();
        gui.setVisible(true);
    }
}