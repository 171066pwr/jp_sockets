package com.mycompany.app.common.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RiverSectionApi {
    SET_RAINFALL(ObjectType.INT, ObjectType.REQUEST_CODE),
    SET_REAL_DISCHARGE(ObjectType.INT, ObjectType.REQUEST_CODE);

    private final ObjectType accepts;
    private final ObjectType returns;
}
