package ro.tuc.ds2020.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Objects;
import java.util.UUID;

public class DeviceDetailsDTO {

    private UUID id;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Max hourly energy consumption is required")
    @Positive(message = "Max hourly energy consumption must be positive")
    private Double maxHourlyEnergyConsumption;

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

    public DeviceDetailsDTO(UUID id, String description, String address, Double maxHourlyEnergyConsumption, UUID userId) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getMaxHourlyEnergyConsumption() {
        return maxHourlyEnergyConsumption;
    }

    public void setMaxHourlyEnergyConsumption(Double maxHourlyEnergyConsumption) {
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
    }

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
