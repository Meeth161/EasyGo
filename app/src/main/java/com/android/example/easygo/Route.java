package com.android.example.easygo;

public class Route {

    String routeNumber;
    String source;
    String destination;

    public Route() {
    }

    public Route(String routeNumber, String source, String destination) {
        this.routeNumber = routeNumber;
        this.source = source;
        this.destination = destination;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public void setRouteNumber(String routeNumber) {
        this.routeNumber = routeNumber;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
