package com.mycompany.app.common.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RiverSectionApi implements CodedEnum {
    ASSIGN_RETENTION_BASIN(ObjectType.REMOTE_INFO, ObjectType.REQUEST_CODE),
    SET_RAINFALL(ObjectType.INT, ObjectType.REQUEST_CODE),
    SET_REAL_DISCHARGE(ObjectType.INT, ObjectType.REQUEST_CODE);

    private final ObjectType accepts;
    private final ObjectType returns;

    @Override
    public boolean matchCode(int code) {
        return this.ordinal() == code;
    }

    public static RiverSectionApi getByCode(int code) {
        return RiverSectionApi.values()[code];
    }
}
