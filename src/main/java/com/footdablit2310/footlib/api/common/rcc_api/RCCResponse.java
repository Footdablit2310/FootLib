package com.footdablit2310.footlib.api.common.rcc_api;

public class RCCResponse {
    private final String status;
    private final String message;
    private final Data data;

    public RCCResponse(String status, String message, Data data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public Data getData() { return data; }
}
