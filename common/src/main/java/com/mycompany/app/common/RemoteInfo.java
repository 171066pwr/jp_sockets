package com.mycompany.app.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RemoteInfo {
    final String host;
    final int port;
    final String name;
    final long delay;

    public RemoteInfo(String info) {
        String[] split = MessageUtil.split(info);
        try {
            host = split[0];
            port = Integer.parseInt(split[1]);
            name = split[2];
            delay = Long.parseLong(split[3]);
        } catch (Exception e) {
            throw new RemoteInfoParseException(info);
        }
    }
    @Override
    public String toString() {
        return MessageUtil.combine(host,port,name,delay);
    }

    static class RemoteInfoParseException extends RuntimeException {
        public RemoteInfoParseException(String message) {
            super("Failed to parse remote info: " + message);
        }
    }
}

