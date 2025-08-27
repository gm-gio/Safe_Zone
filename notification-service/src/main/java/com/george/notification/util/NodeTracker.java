package com.george.notification.util;

import lombok.RequiredArgsConstructor;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class NodeTracker {

    private final DiscoveryClient client;
    private final Map<String, Integer> serviceCounts = new HashMap<>();


    public Integer getActiveNodeCount(String serviceName) {
        serviceCounts.putIfAbsent(serviceName, getServiceInstanceCount(serviceName));
        return serviceCounts.get(serviceName);
    }


    @Scheduled(fixedDelay = 5000)
    private void serviceMonitor() {
        if (!serviceCounts.isEmpty()) {
            serviceCounts.replaceAll((serviceName, dummy) -> getServiceInstanceCount(serviceName));
        }
    }


    private Integer getServiceInstanceCount(String serviceName) {
        return client.getInstances(serviceName).size();
    }

}
