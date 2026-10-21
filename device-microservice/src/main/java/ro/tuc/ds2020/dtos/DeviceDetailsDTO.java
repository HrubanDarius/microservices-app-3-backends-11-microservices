package ro.tuc.ds2020.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Objects;
import java.util.UUID;

public class DeviceDetailsDTO {

    @JsonProperty("id")
    private UUID id;

    @NotBlank(message = "Description is required")
    @JsonProperty("description")
    private String description;

    @NotBlank(message = "Address is required")
    @JsonProperty("address")
    private String address;

    @NotNull(message = "Max hourly energy consumption is required")
    @Positive(message = "Max hourly energy consumption must be positive")
    @JsonProperty("maxHourlyEnergyConsumption")
    private Double maxHourlyEnergyConsumption;

    @JsonProperty("userId")
    private UUID userId;

    public DeviceDetailsDTO() {
    }

    public DeviceDetailsDTO(String description, String address, Double maxHourlyEnergyConsumption) {
        this.description = description;
        this.address = address;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
    }

    public DeviceDetailsDTO(String description, String address, Double maxHourlyEnergyConsumption, UUID userId) {
        this.description = description;
        this.address = address;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
        this.userId = userId;
    }

    @JsonCreator
    public DeviceDetailsDTO(
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

    @JsonProperty("id")
    public void setId(UUID id) {
        this.id = id;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("maxHourlyEnergyConsumption")
    public Double getMaxHourlyEnergyConsumption() {
        return maxHourlyEnergyConsumption;
    }

    @JsonProperty("maxHourlyEnergyConsumption")
    public void setMaxHourlyEnergyConsumption(Double maxHourlyEnergyConsumption) {
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
    }

    @JsonProperty("userId")
    public UUID getUserId() {
        return userId;
    }

    @JsonProperty("userId")
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
        DeviceDetailsDTO that = (DeviceDetailsDTO) o;
        return Objects.equals(id, that.id)
                && Objects.equals(description, that.description)
                && Objects.equals(address, that.address)
                && Objects.equals(maxHourlyEnergyConsumption, that.maxHourlyEnergyConsumption)
                && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, address, maxHourlyEnergyConsumption, userId);
    }
}
