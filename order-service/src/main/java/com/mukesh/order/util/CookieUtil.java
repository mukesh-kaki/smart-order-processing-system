package com.mukesh.order.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public final class CookieUtil {

    private CookieUtil() {
    }

    public static String getDeviceId(
            HttpServletRequest request
    ) {

        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {

            if ("DEVICE_ID".equals(cookie.getName())) {

                return cookie.getValue();

            }

        }

        return null;

    }

}