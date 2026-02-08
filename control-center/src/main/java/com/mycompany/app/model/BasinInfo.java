package com.mycompany.app.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class BasinInfo {
    Integer fillingPercentage;
    Integer dischargeRate;

    @Override
    public String toString() {
        return String.format("[%d%%,%d]", fillingPercentage, dischargeRate);
    }
}
