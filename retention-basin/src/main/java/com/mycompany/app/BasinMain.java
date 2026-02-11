package com.mycompany.app;

import com.mycompany.app.common.RemoteInfo;
import com.mycompany.app.common.Response;
import com.mycompany.app.common.ResponseCode;
import com.mycompany.app.gui.BasinFrame;
import com.mycompany.app.model.RetentionBasin;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class BasinMain {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("Pass register port, name, volume, ControlCenter remote info (host:port:name), inflow RiverSection remote info (host:port:name)");
            BasinFrame gui = new BasinFrame();
            gui.pack();
            gui.setSize(550, 350);
            gui.setVisible(true);
        } else {
            int port = Integer.parseInt(args[0]);
            String name = args[1];
            int volume = Integer.parseInt(args[2]);
            RemoteInfo controlCenter = new RemoteInfo(args[3]);
            RetentionBasin basin = new RetentionBasin(port, name, volume);
            basin.setControlCenter(controlCenter);
            BasinFrame gui = new BasinFrame(basin);
            new Thread(basin).start();

//            for (int i = 4; i < args.length; i++) {
//                RemoteInfo riverSection = new RemoteInfo(args[i]);
//                subscribeToRemote(basin, riverSection);
//            }
            //gui.pack();
            gui.setSize(550, 400);
            gui.setVisible(true);
        }
    }

    static void subscribeToRemote(RetentionBasin obj, RemoteInfo remote) {
        Response response = null;
        int retries = 0;
        while (retries < 5 ||response == null || !ResponseCode.YES.equals(response.getCode())) {
            try {
                response = obj.subscribeToRemote(remote);
                log.info(response);
                Thread.sleep(1000);
                retries++;
            } catch (Exception e) {
                log.error("Failed to subscribe to remote" + e.getMessage());
            }
        }
    }
}