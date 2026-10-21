package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.dtos.UserSyncDTO;

import java.util.Collections;
import java.util.UUID;

@Service
public class DeviceServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceServiceClient.class);

    private final RestTemplate restTemplate;

    @Value("${device.service.url:http://device-service:8081/device-service}")
    private String deviceServiceUrl;

    @Autowired
    public DeviceServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void syncUserCreation(UUID userId) {
        String primaryUrl = deviceServiceUrl + "/user";
        UserSyncDTO syncDTO = new UserSyncDTO(userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<UserSyncDTO> requestEntity = new HttpEntity<>(syncDTO, headers);

        try {
            LOGGER.info("Sending sync user creation to device-service at {} for userId: {}", primaryUrl, userId);
            ResponseEntity<UUID> response = restTemplate.postForEntity(primaryUrl, requestEntity, UUID.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalStateException("Failed to sync user creation with device-service. HTTP status: " + response.getStatusCode());
            }
        } catch (ResourceAccessException ex) {
            if (deviceServiceUrl.contains("device-service")) {
                String fallbackUrl = "http://localhost/device-service/user";
                LOGGER.warn("Could not reach {} directly. Retrying via fallback URL: {}", primaryUrl, fallbackUrl);
                ResponseEntity<UUID> response = restTemplate.postForEntity(fallbackUrl, requestEntity, UUID.class);
                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new IllegalStateException("Failed to sync user creation with device-service via fallback. HTTP status: " + response.getStatusCode());
                }
                return;
            }
            throw ex;
        }
    }

    public void syncUserDeletion(UUID userId) {
        String primaryUrl = deviceServiceUrl + "/user/" + userId;

        try {
            LOGGER.info("Sending sync user deletion to device-service at {} for userId: {}", primaryUrl, userId);
            restTemplate.delete(primaryUrl);
        } catch (ResourceAccessException ex) {
            if (deviceServiceUrl.contains("device-service")) {
                String fallbackUrl = "http://localhost/device-service/user/" + userId;
                LOGGER.warn("Could not reach {} directly. Retrying via fallback URL: {}", primaryUrl, fallbackUrl);
                restTemplate.delete(fallbackUrl);
                return;
            }
            throw ex;
        }
    }
}
