package com.mycompany.app.common.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RetentionBasin {
    GET_WATER_DISCHARGE(ObjectType.VOID, ObjectType.INT),
    GET_FILLING_PERENTAGE(ObjectType.VOID, ObjectType.INT),
    SET_WATER_DISCHARGE(ObjectType.INT, ObjectType.REQUEST_CODE),
    SET_WATER_INFLOW(ObjectType.INT, ObjectType.REQUEST_CODE);

    private final ObjectType accepts;
    private final ObjectType returns;
}
