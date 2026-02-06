package com.mycompany.app.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RequestData {
    private final int code;
    private final String source;

    @Override
    public String toString() {
        return MessageSplitter.combine(code,source);
    }
}
