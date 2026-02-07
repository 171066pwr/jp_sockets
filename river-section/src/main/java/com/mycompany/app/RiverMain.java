package com.mycompany.app;

import com.mycompany.app.common.Response;
import com.mycompany.app.model.RiverSection;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class RiverMain {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Pass register port and name in parameter");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        String name = args[1];
        int baseFlow = args.length < 3 ? 10: Integer.parseInt(args[2]);
        long delay = args.length < 4 ? 5000: Long.parseLong(args[3]);

        RiverSection section = new RiverSection(port, name, baseFlow, delay);

        try{
            Response response = section.subscribeToRemote("localhost", 4444);
            log.info(response);
        } catch (Exception e){
            throw new RuntimeException("Failed to subscribe to remote" + e.getMessage());
        }
    }
}