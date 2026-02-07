package com.mycompany.app.common.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnvironmentApi implements CodedEnum {
    ASSIGN_RIVER_SECTION(ObjectType.REMOTE_INFO, ObjectType.REQUEST_CODE);

    private final ObjectType accepts;
    private final ObjectType returns;

    @Override
    public boolean matchCode(int code) {
        return this.ordinal() == code;
    }
}
