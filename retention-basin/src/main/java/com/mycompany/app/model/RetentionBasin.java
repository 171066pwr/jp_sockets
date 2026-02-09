package com.mycompany.app.model;

import com.mycompany.app.common.*;
import com.mycompany.app.common.api.RetentionBasinApi;
import com.mycompany.app.common.api.RiverSectionApi;
import lombok.extern.log4j.Log4j2;

import java.util.*;

@Log4j2
public class RetentionBasin extends Service implements RemoteSubscriber, Runnable {
    private final int volume;
    private final Map<String, Integer> currentInflows = new HashMap<String, Integer>();
    private RemoteInfo controlCenter;
    private int currentVolume;
    private int realDischargeRate = 0;
    private int currentPlannedDischarge = 0;

    public RetentionBasin(int port, String name, int volume) {
        super(port, name, 0);
        this.volume = volume;
        currentVolume = volume/2;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            tick();
        }
    }

    @Override
    public Response handleRequest(Request request) {
        Response response = null;
        try {
            RetentionBasinApi method = RetentionBasinApi.getByCode(request.getCode());
            switch (method) {
                case ASSIGN_RIVER_SECTION -> {
                    RemoteInfo remote = new RemoteInfo(request.getData());
                    if (addRemote(remote))  {
                        response = new Response(ResponseCode.YES, name);
                    }
                }
                case SET_WATER_INFLOW -> {
                    updateSourceInflow(request.getSourceName(), Integer.parseInt(request.getData()));
                    response = new Response(ResponseCode.YES, name);
                    log.info("Inflow updated [{}]: {}", request.getSourceName(), request.getData());
                }
                case SET_WATER_DISCHARGE -> {
                    if(isControlCenter(request.getSourceName())) {
                    currentPlannedDischarge = Integer.parseInt(request.getData());
                    response = new Response(ResponseCode.YES, name);
                    log.info("Discharge rate set [{}]: {}", request.getSourceName(), request.getData());
                    } else {
                        response = new Response(ResponseCode.NO, name + " Unauthorized");
                    }
                }
                case GET_WATER_DISCHARGE -> {
                    if(isControlCenter(request.getSourceName())) {
                        response = new Response(ResponseCode.YES, Integer.toString(currentPlannedDischarge));
                        log.info("Get discharge rate [{}]: {}", request.getSourceName(), request.getData());
                    } else {
                        response = new Response(ResponseCode.NO, name + " Unauthorized");
                    }
                }
                case GET_FILLING_PERCENTAGE -> {
                    if(isControlCenter(request.getSourceName())) {
                        response = new Response(ResponseCode.YES, Integer.toString(getFillingPercentage()));
                        log.info("Get filling percentage [{}]: {}", request.getSourceName(), request.getData());
                    } else {
                        response = new Response(ResponseCode.NO, name + " Unauthorized");
                    }
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
    public Response subscribeToRemote(RemoteInfo remote) {
        return updateRemote(remote, RiverSectionApi.ASSIGN_RETENTION_BASIN, getRemoteInfo().toString());
    }

    @Override
    public void handleException(Exception e) {
        log.error(e);
    }

    public void setControlCenter(RemoteInfo remote) {
        Response response = subscribeWithDelay(remote, 5000);
        if ( ResponseCode.YES.equals(response.getCode()) ) {
            controlCenter = remote;
        }
        log.info("Control Center subscribed: {}", response);
    }

    private boolean isControlCenter(String remoteName) {
        return controlCenter != null && Objects.equals(controlCenter.getName(), remoteName);
    }

    private void tick() {
        currentVolume += getCurrentInflows();
        if (currentVolume > volume) {
            realDischargeRate = currentPlannedDischarge + currentVolume - volume;
        }
        realDischargeRate = Math.min(currentPlannedDischarge, currentVolume);
        currentVolume -= realDischargeRate;
        updateAllRemotes();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error(e);
        }
    }

    private void updateSourceInflow(String source, int inflow) {
        currentInflows.put(source, inflow);
    }

    private void updateRemote(RemoteInfo info) {
        Response response = updateRemote(info.getHost(), info.getPort(), RiverSectionApi.SET_REAL_DISCHARGE, String.valueOf(getOutflowPerRemote()));
        log.info(response);
    }

    private void updateAllRemotes() {
        for(RemoteInfo remote : getRemoteSet()) {
            updateRemote(remote);
        }
    }

    private int getOutflowPerRemote() {
        return realDischargeRate / getOutflowNumber();
    }

    private int getOutflowNumber() {
        return getRemoteSet().size();
    }

    private int getFillingPercentage() {
        return (currentVolume*100)/volume;
    }

    private int getCurrentInflows() {
        return currentInflows.values().stream().reduce(0, Integer::sum);
    }
}
