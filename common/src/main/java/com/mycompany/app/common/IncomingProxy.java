package com.mycompany.app.common;

public abstract class IncomingProxy extends ServerWrapper {
    public IncomingProxy(int port) {
        super(port);
    }

    @Override
    protected Object handleRequest(String info) {
        String[] tokens = MessageSplitter.split(info);
        return processRequest(new RequestData(Integer.parseInt(tokens[0]), tokens[1]));
    }

    protected abstract String processRequest(RequestData request);
}
