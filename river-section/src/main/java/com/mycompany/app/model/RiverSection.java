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
    @Getter
    private final long delay;
    private final int baseFlow;
    private final Map<String, Integer> currentInflows = new HashMap<String, Integer>();

    public RiverSection(int port, String name, int baseFlow, long delay) {
        super(name, port);
        this.baseFlow = baseFlow;
        this.delay = delay;
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
                    new Response(ResponseCode.YES, name);
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
    public Response subscribeToRemote(String host, int port) {
        return updateRemote(host, port, EnvironmentApi.ASSIGN_RIVER_SECTION.ordinal(), getRemoteInfo().toString());
    }

    public int getCurrentInflows() {
        return currentInflows.values().stream().reduce(0, Integer::sum);
    }

    public int getTotalOutflow() {
        return baseFlow + getCurrentInflows();
    }

    private void updateRemoteBasin(RemoteInfo info) {
        Response response = updateRemote(info.getHost(), info.getPort(), RetentionBasinApi.SET_WATER_INFLOW.ordinal(), String.valueOf(getTotalOutflow()));
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
