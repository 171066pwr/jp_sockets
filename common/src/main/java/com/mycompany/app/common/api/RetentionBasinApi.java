package com.mycompany.app.common.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RetentionBasinApi implements CodedEnum {
    ASSIGN_RIVER_SECTION(ObjectType.REMOTE_INFO, ObjectType.REQUEST_CODE),
    GET_WATER_DISCHARGE(ObjectType.VOID, ObjectType.INT),
    GET_FILLING_PERCENTAGE(ObjectType.VOID, ObjectType.INT),
    SET_WATER_DISCHARGE(ObjectType.INT, ObjectType.REQUEST_CODE),
    SET_WATER_INFLOW(ObjectType.INT, ObjectType.REQUEST_CODE);

    private final ObjectType accepts;
    private final ObjectType returns;

    @Override
    public boolean matchCode(int code) {
        return this.ordinal() == code;
    }

    public static RetentionBasinApi getByCode(int code) {
        return RetentionBasinApi.values()[code];
    }
}
