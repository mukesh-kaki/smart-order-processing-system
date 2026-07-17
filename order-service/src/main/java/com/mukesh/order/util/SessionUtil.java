package com.mukesh.order.util;

import com.mukesh.order.dto.SessionInfo;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public final class SessionUtil {

    private SessionUtil() {
    }

    public static SessionInfo extract(HttpServletRequest request) {

        String userAgent = request.getHeader("User-Agent");

        String ipAddress = getClientIp(request);

        String deviceName = buildDeviceName(userAgent);

        return new SessionInfo(
                deviceName,
                userAgent,
                ipAddress,
                UUID.randomUUID().toString()
        );
    }

    private static String getClientIp(HttpServletRequest request) {

        String forwarded =
                request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {

            return forwarded.split(",")[0].trim();

        }

        return request.getRemoteAddr();
    }

    private static String buildDeviceName(String userAgent) {

        if (userAgent == null) {
            return "Unknown Device";
        }

        if (userAgent.contains("Windows"))
            return "Windows";

        if (userAgent.contains("Mac"))
            return "Mac";

        if (userAgent.contains("Android"))
            return "Android";

        if (userAgent.contains("iPhone"))
            return "iPhone";

        if (userAgent.contains("Linux"))
            return "Linux";

        return "Unknown Device";
    }

}