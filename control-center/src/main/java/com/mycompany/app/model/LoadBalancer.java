package com.mycompany.app.model;

import com.mycompany.app.common.RemoteInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Log4j2
@AllArgsConstructor
public class LoadBalancer implements Runnable {
    private final ControlCenter controlCenter;
    private final Map<RemoteInfo, BasinInfo> basinsTracker = new HashMap<>();
    private int targetLevel = 50;
    private long period = 2000;

    public LoadBalancer(ControlCenter controlCenter) {
        this.controlCenter = controlCenter;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Map<RemoteInfo, BasinInfo> results = controlCenter.queryRemotes();

            for (Map.Entry<RemoteInfo, BasinInfo> entry : results.entrySet()) {
                BasinInfo current = entry.getValue();
                if(current.fillingPercentage == null && current.dischargeRate == null) {
                    continue;
                }
                if (!basinsTracker.containsKey(entry.getKey())) {
                    basinsTracker.put(entry.getKey(), current);
                }
                BasinInfo former = basinsTracker.get(entry.getKey());
                int dischargeRate = calculateDischarge(former, current);

                controlCenter.setDischargeRate(entry.getKey(), dischargeRate);
            }
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                log.error(e);
            }
        }
    }

    private int calculateDischarge(BasinInfo former, BasinInfo current) {
        int diff = current.fillingPercentage - former.fillingPercentage;
        int targetdiff = current.fillingPercentage - targetLevel;
        double multiplier =  1 + Math.max(-1, (targetdiff * 0.01 + diff * 0.02));
        return (int) ((10 + current.dischargeRate) * multiplier);
    }
}

