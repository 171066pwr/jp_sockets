package com.mycompany.app.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public class Response {
    private final ResponseCode code;
    private final String data;

    public Response(ResponseCode code) {
        this(code, "");
    }

    public Response(String message) {
        String[] tokens = MessageUtil.split(message);
        try {
            code = ResponseCode.valueOf(tokens[0]);
            data = Arrays.stream(tokens).skip(1).reduce(String::concat).orElse("");
        } catch (Exception e) {
            throw new ResponseParseException(message);
        }
    }

    @Override
    public String toString() {
        return MessageUtil.combine(code, data);
    }

    static class ResponseParseException extends RuntimeException {
        public ResponseParseException(String message) {
            super("Failed to parse response: " + message);
        }
    }
}
