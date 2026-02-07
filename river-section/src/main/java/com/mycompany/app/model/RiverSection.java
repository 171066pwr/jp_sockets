package com.mycompany.app.model;

import com.mycompany.app.common.*;
import com.mycompany.app.common.api.EnvironmentApi;
import com.mycompany.app.common.api.RetentionBasinApi;
import com.mycompany.app.common.api.RiverSectionApi;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Log4j2
public class RiverSection extends Service implements RemoteSubscriber {
    private final int baseFlow;
    private final Map<String, Integer> currentInflows = new HashMap<String, Integer>();

    public RiverSection(int port, String name, int baseFlow, long delay) {
        super(port, name, delay);
        this.baseFlow = baseFlow;
    }

    @Override
    public Response handleRequest(Request request) {
        Response response = null;
        try {
            RiverSectionApi method = RiverSectionApi.getByCode(request.getCode());
            switch (method) {
                case ASSIGN_RETENTION_BASIN -> {
                    RemoteInfo remote = new RemoteInfo(request.getData());
                    if (remoteSet.add(remote))  {
                        response = new Response(ResponseCode.YES, name);
                        updateRemoteBasin(remote);
                    }
                }
                case SET_RAINFALL -> {
                    updateSourceInflow(request.getSourceName(), Integer.parseInt(request.getData()));
                    response = new Response(ResponseCode.YES, name);
                    updateAllRemotes();
                    log.info("Rainfall received [{}]: {}", request.getSourceName(), request.getData());
                }
                case SET_REAL_DISCHARGE -> {
                    delayedUpdateSourceInflow(request.getSourceName(), Integer.parseInt(request.getData()));
                    response = new Response(ResponseCode.YES, name);
                    log.info("Discharge received [{}]: {}", request.getSourceName(), request.getData() + " (effective after " + delay + ")");
                }
            }
        } catch (Exception e) {
            if(e instanceof IndexOutOfBoundsException) {
                e = new UnsupportedOperationException("Unsupported Code: " + request.getCode());
            }
            response = new Response(ResponseCode.ERROR, name + ": " + e.getMessage());
        }
        return response == null ? new Response(ResponseCode.NO, name) : response;
    }

    @Override
    public void handleException(Exception e) {
        log.error(e);
    }

    @Override
    public Response subscribeToRemote(RemoteInfo remote) {
        return updateRemote(remote, EnvironmentApi.ASSIGN_RIVER_SECTION, getRemoteInfo().toString());
    }

    private int getCurrentInflows() {
        return currentInflows.values().stream().reduce(0, Integer::sum);
    }

    private int getTotalOutflow() {
        return baseFlow + getCurrentInflows();
    }

    private void updateRemoteBasin(RemoteInfo info) {
        Response response = updateRemote(info, RetentionBasinApi.SET_WATER_INFLOW, String.valueOf(getTotalOutflow()));
        log.info(response);
    }

    private void delayedUpdateSourceInflow(String source, int inflow) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                currentInflows.put(source, inflow);
                updateAllRemotes();
            }
        }, delay);
    }

    private void updateSourceInflow(String source, int inflow) {
        currentInflows.put(source, inflow);
    }

    private void updateRemote(RemoteInfo info) {
        Response response = updateRemote(info.getHost(), info.getPort(), RetentionBasinApi.SET_WATER_INFLOW, String.valueOf(getTotalOutflow()));
        log.info(response);
    }

    private void updateAllRemotes() {
        for(RemoteInfo remote : remoteSet) {
            updateRemote(remote);
        }
    }
}
