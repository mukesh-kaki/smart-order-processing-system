package com.mukesh.order.dto;

public record SessionInfo(

        String deviceName,

        String userAgent,

        String ipAddress,

        String deviceId

) {
}