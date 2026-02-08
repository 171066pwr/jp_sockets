package com.mycompany.app.model;

import com.mycompany.app.common.*;
import com.mycompany.app.common.api.EnvironmentApi;
import com.mycompany.app.common.api.RiverSectionApi;
import lombok.extern.log4j.Log4j2;

import java.util.Random;

@Log4j2
public class Environment extends Service {
    private final Random rand = new Random();
    private int currentRainfall;

    public Environment(int port, String name) {
        super(port, name, 0);
    }

    @Override
    public Response handleRequest(Request request) {
        Response response = null;
        try {
            if (EnvironmentApi.ASSIGN_RIVER_SECTION.matchCode(request.getCode())) {
                RemoteInfo remote = new RemoteInfo(request.getData());
                if (remoteSet.add(remote))  {
                    response = new Response(ResponseCode.YES, name);
                    updateRemote(remote);
                }
            } else {
                throw new UnsupportedOperationException("Code " + request.getCode());
            }
        } catch (Exception e) {
            response = new Response(ResponseCode.ERROR, name + ": " + e.getMessage());
        }
        response = response == null ? new Response(ResponseCode.NO, name) : response;
        log.info("Request {}: {}", request, response);
        return response;
    }

    @Override
    public void handleException(Exception e) {
        log.error(e);
    }

    public void setCurrentRainfall(int currentRainfall) {
        this.currentRainfall = currentRainfall;
        updateRainfall();
    }

    public void updateRainfall() {
        updateAllRemotes();
        log.info(String.format("Rainfall: %d", currentRainfall));
    }

    private void updateAllRemotes() {
        for(RemoteInfo remote : remoteSet) {
            updateRemote(remote);
        }
    }

    private void updateRemote(RemoteInfo info) {
        Response response = updateRemote(info, RiverSectionApi.SET_RAINFALL, String.valueOf(currentRainfall));
        log.info(response);
    }
}
