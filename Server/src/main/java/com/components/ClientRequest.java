package com.components;

public class ClientRequest {

    private long firstRequest;
    private long lastRequest;
    private int numOfRequests;

    public ClientRequest(long firstRequest, long lastRequest, int numOfRequests) {
        this.firstRequest = firstRequest;
        this.lastRequest = lastRequest;
        this.numOfRequests = numOfRequests;
    }

    public long getFirstRequest() {
        return firstRequest;
    }

    public void setFirstRequest(long firstRequest) {
        this.firstRequest = firstRequest;
    }

    public long getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(long lastRequest) {
        this.lastRequest = lastRequest;
    }

    public int getNumOfRequests() {
        return numOfRequests;
    }

    public void setNumOfRequests(int numOfRequests) {
        this.numOfRequests = numOfRequests;
    }
}