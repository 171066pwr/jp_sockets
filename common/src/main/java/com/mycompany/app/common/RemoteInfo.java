package com.mycompany.app.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

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
            if (split.length > 3) {
                delay = Long.parseLong(split[3]);
            } else {
                delay = 0;
            }
        } catch (Exception e) {
            throw new RemoteInfoParseException(info);
        }
    }
    @Override
    public String toString() {
        return MessageUtil.combine(host,port,name,delay);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoteInfo remoteInfo = (RemoteInfo) o;

        return Objects.equals(this.host, remoteInfo.host)
        && Objects.equals(this.name, remoteInfo.name)
        && this.port == remoteInfo.port;
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, name, port);
    }

    public static class RemoteInfoParseException extends RuntimeException {
        public RemoteInfoParseException(String message) {
            super("Failed to parse remote info: " + message);
        }
    }
}

