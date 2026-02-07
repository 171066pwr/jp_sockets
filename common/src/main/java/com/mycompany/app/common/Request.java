package com.mycompany.app.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public class Request {
    private final int code;
    private final String sourceName;
    private final String data;

    Request(String message) {
        String[] tokens = MessageUtil.split(message);
        try {
            code = Integer.parseInt(tokens[0]);
            sourceName = tokens[1];
            data = Arrays.stream(tokens).skip(2).reduce(MessageUtil::combine).orElse("");
        } catch (Exception e) {
            throw new RequestParseException(message);
        }
    }

    @Override
    public String toString() {
        return MessageUtil.combine(code, sourceName, data);
    }

    static class RequestParseException extends RuntimeException {
        public RequestParseException(String message) {
            super("Failed to parse request: " + message);
        }
    }
}
