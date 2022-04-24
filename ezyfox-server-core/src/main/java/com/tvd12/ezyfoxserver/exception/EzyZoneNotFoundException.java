package com.tvd12.ezyfoxserver.exception;

public class EzyZoneNotFoundException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    public EzyZoneNotFoundException(String zoneName) {
        super("zone: " + zoneName + " not found");
    }

    public EzyZoneNotFoundException(int zoneId) {
        super("zone with id: " + zoneId + " not found");
    }
}
