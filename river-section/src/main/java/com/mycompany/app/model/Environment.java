package com.mycompany.app.model;

import com.mycompany.app.common.api.RiverSectionApi;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.Random;

@Log4j2
public class Environment implements SocketProxyListener, Runnable {
    private final OutgoingProxy outgoingProxy;
    private final Random rand = new Random();
    private final int period;
    private int currentRainfall;

    public Environment(int port, int timeout, int period) {
        this.period = period;
        outgoingProxy = new OutgoingProxy(port, timeout);
        outgoingProxy.addListener(this);
        new Thread(outgoingProxy).start();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            updateAndGetRainfall();
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void acceptSource(RemoteInfo info) {
        outgoingProxy.updateRemote(RiverSectionApi.SET_RAINFALL.ordinal(), String.valueOf(currentRainfall), info);
    }

    public int updateAndGetRainfall() {
        int roll = rand.nextInt(100);
        currentRainfall = roll < 70 ? 0 : roll - 70;
        updateAllRemotes();
        return currentRainfall;
    }

    private void updateAllRemotes() {
        Map<RemoteInfo, String> responses = outgoingProxy.updateRemotes(RiverSectionApi.SET_RAINFALL.ordinal(), String.valueOf(currentRainfall));
        for(Map.Entry<RemoteInfo, String> entry : responses.entrySet()) {
            String[] response = MessageSplitter.split(entry.getValue());
            switch (ResponseCode.valueOf(response[0])) {
                case ERROR -> log.error(response[1]);
                default -> log.info(response[0]);
            }
        }
    }
}
