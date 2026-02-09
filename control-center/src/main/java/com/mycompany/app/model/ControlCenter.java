package com.mycompany.app.model;

import com.mycompany.app.common.*;
import com.mycompany.app.common.api.ControlCenterApi;
import com.mycompany.app.common.api.RetentionBasinApi;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ControlCenter extends Service {
    public ControlCenter(int port, String name) {
        super(port, name, 0);
    }

    @Override
    public Response handleRequest(Request request) {
        Response response = null;
        try {
            if (ControlCenterApi.ASSIGN_RETENTION_BASIN.matchCode(request.getCode())) {
                RemoteInfo remote = new RemoteInfo(request.getData());
                if (addRemote(remote))  {
                    response = new Response(ResponseCode.YES, name);
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

    void setDischargeRate(RemoteInfo remote, int dischargeRate) {
        Response response = updateRemote(remote, RetentionBasinApi.SET_WATER_DISCHARGE, Integer.toString(dischargeRate));
        log.info("{}: Water discharge set to {}: {}", remote, dischargeRate, response);
    }

    Map<RemoteInfo, BasinInfo> queryRemotes() {
        Map<RemoteInfo, BasinInfo> map = new HashMap<>();
        for (RemoteInfo remote : getRemoteSet()) {
            BasinInfo info = new BasinInfo(getFillingPercentage(remote), getDischargeRate(remote));
            map.put(remote, info);
            log.info("Remote {}: {}", remote, info);
        }
        return map;
    }

    private Integer getDischargeRate(RemoteInfo remote) {
        Response response = updateRemote(remote, RetentionBasinApi.GET_WATER_DISCHARGE, "");
        Integer value = null;
        if (response.getCode() == ResponseCode.YES) {
            value = Integer.parseInt(response.getData());
        }
        return value;
    }

    private Integer getFillingPercentage(RemoteInfo remote) {
        Response response = updateRemote(remote, RetentionBasinApi.GET_FILLING_PERCENTAGE, "");
        Integer value = null;
        if (response.getCode() == ResponseCode.YES) {
            value = Integer.parseInt(response.getData());
        }
        return value;
    }

    @Override
    public void handleException(Exception e) {
        log.error(e);
    }
}
