package ro.tuc.ds2020.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

public class DeviceDTO {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("description")
    private String description;

    @JsonProperty("address")
    private String address;

    @JsonProperty("maxHourlyEnergyConsumption")
    private Double maxHourlyEnergyConsumption;

    @JsonProperty("userId")
    private UUID userId;

    public DeviceDTO() {
    }

    public DeviceDTO(UUID id, String description, Double maxHourlyEnergyConsumption) {
        this.id = id;
        this.description = description;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
    }

    public DeviceDTO(UUID id, String description, Double maxHourlyEnergyConsumption, UUID userId) {
        this.id = id;
        this.description = description;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
        this.userId = userId;
    }

    @JsonCreator
    public DeviceDTO(
            @JsonProperty("id") UUID id,
            @JsonProperty("description") String description,
            @JsonProperty("address") String address,
            @JsonProperty("maxHourlyEnergyConsumption") Double maxHourlyEnergyConsumption,
            @JsonProperty("userId") UUID userId) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
        this.userId = userId;
    }

    @JsonProperty("id")
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("maxHourlyEnergyConsumption")
    public Double getMaxHourlyEnergyConsumption() {
        return maxHourlyEnergyConsumption;
    }

    public void setMaxHourlyEnergyConsumption(Double maxHourlyEnergyConsumption) {
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
    }

    @JsonProperty("userId")
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeviceDTO deviceDTO = (DeviceDTO) o;
        return Objects.equals(id, deviceDTO.id)
                && Objects.equals(description, deviceDTO.description)
                && Objects.equals(address, deviceDTO.address)
                && Objects.equals(maxHourlyEnergyConsumption, deviceDTO.maxHourlyEnergyConsumption)
                && Objects.equals(userId, deviceDTO.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, address, maxHourlyEnergyConsumption, userId);
    }
}
