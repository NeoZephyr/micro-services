package com.pain.blaze.common.service;

import lombok.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class ServiceConfig {
    private int security; // Public, Authenticated, or Admin
    private boolean restrictDev; // If true, service is suppressed in stage and prod
    private String backendDomain;  // Backend service to query
    private boolean noCacheHtml; // If true, injects a header for HTML responses telling the browser not to cache HTML

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static Map<String, ServiceConfig> serviceMap;

    static {
        serviceMap = new HashMap<>();

        ServiceConfig serviceConfig = ServiceConfig.builder()
                .security(SecurityConstant.SEC_AUTHENTICATED)
                .restrictDev(false)
                .backendDomain("account-service")
                .build();
        serviceMap.put("account", serviceConfig);

        serviceConfig = ServiceConfig.builder()
                .security(SecurityConstant.SEC_AUTHENTICATED)
                .restrictDev(false)
                .backendDomain("myaccount-service")
                .noCacheHtml(true)
                .build();
        serviceMap.put("myaccount", serviceConfig);

        serviceConfig = ServiceConfig.builder()
                .security(SecurityConstant.SEC_AUTHENTICATED)
                .restrictDev(true)
                .backendDomain("superpowers-service")
                .build();
        serviceMap.put("superpowers", serviceConfig);

        serviceConfig = ServiceConfig.builder()
                .security(SecurityConstant.SEC_AUTHENTICATED)
                .restrictDev(false)
                .backendDomain("whoami-service")
                .build();
        serviceMap.put("whoami", serviceConfig);

        serviceConfig = ServiceConfig.builder()
                .security(SecurityConstant.SEC_AUTHENTICATED)
                .restrictDev(false)
                .backendDomain("app-service")
                .noCacheHtml(true)
                .build();
        serviceMap.put("app", serviceConfig);

        serviceConfig = ServiceConfig.builder()
                .security(SecurityConstant.SEC_AUTHENTICATED)
                .restrictDev(false)
                .backendDomain("company_service")
                .build();
        serviceMap.put("company", serviceConfig);

        // // Debug site for faraday proxy
        serviceConfig = ServiceConfig.builder()
                .security(SecurityConstant.SEC_PUBLIC)
                .restrictDev(true)
                .backendDomain("httpbin.org")
                .build();
        serviceMap.put("faraday", serviceConfig);

        serviceConfig = ServiceConfig.builder()
                .security(SecurityConstant.SEC_PUBLIC)
                .restrictDev(false)
                .backendDomain("ical-service")
                .build();
        serviceMap.put("ical", serviceConfig);

        serviceConfig = ServiceConfig.builder()
                .security(SecurityConstant.SEC_PUBLIC)
                .restrictDev(false)
                .backendDomain("www-service")
                .build();
        serviceMap.put("www", serviceConfig);

        serviceMap = Collections.unmodifiableMap(serviceMap);
    }

    public static Map<String, ServiceConfig> getServiceMap() {
        return serviceMap;
    }
}
