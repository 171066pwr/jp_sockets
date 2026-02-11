package com.mycompany.app;

import com.mycompany.app.common.RemoteInfo;
import com.mycompany.app.common.Response;
import com.mycompany.app.common.ResponseCode;
import com.mycompany.app.model.RiverSection;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class RiverMain {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Pass register port, name, base flow, delay, [optional] RetentionBasin remote info (host:port:name)");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        String name = args[1];
        int baseFlow = args.length < 3 ? 10: Integer.parseInt(args[2]);
        long delay = args.length < 4 ? 5000: Long.parseLong(args[3]);
        RiverSection section = new RiverSection(port, name, baseFlow, delay);

        subscribeToRemote(section, "localhost", 4444);
        if (args.length > 4) {
            RemoteInfo basin = new RemoteInfo(args[4]);
            subscribeToRemote(section, basin.getHost(), basin.getPort());
        }
    }

    static void subscribeToRemote(RiverSection obj, String host, int port) {
        Response response = null;
        int retries = 0;
        while (retries < 5 || response == null || !ResponseCode.YES.equals(response.getCode())) {
            try {
                response = obj.subscribeToRemote(host, port);
                log.info(response);
                Thread.sleep(1000);
            } catch (Exception e) {
                log.error("Failed to subscribe to remote" + e.getMessage());
            }
        }
    }
}