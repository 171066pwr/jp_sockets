package com.mycompany.app.common.api;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ObjectType {
    INT,
    LONG,
    STRING,
    BOOLEAN,
    REMOTE_INFO,
    REQUEST_CODE,
    VOID
}
