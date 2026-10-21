package ro.tuc.ds2020.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "device")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "max_hourly_energy_consumption", nullable = false)
    private Double maxHourlyEnergyConsumption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserReference user;

    public Device() {
    }

    public Device(String description, String address, Double maxHourlyEnergyConsumption) {
        this.description = description;
        this.address = address;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
    }

    public Device(String description, String address, Double maxHourlyEnergyConsumption, UUID userId) {
        this.description = description;
        this.address = address;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
        setUserId(userId);
    }

    public Device(UUID id, String description, String address, Double maxHourlyEnergyConsumption, UUID userId) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
        setUserId(userId);
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

    public UserReference getUser() {
        return user;
    }

    public void setUser(UserReference user) {
        this.user = user;
    }

    public UUID getUserId() {
        return user != null ? user.getId() : null;
    }

    public void setUserId(UUID userId) {
        if (userId != null) {
            this.user = new UserReference(userId);
        } else {
            this.user = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Device device = (Device) o;
        return Objects.equals(id, device.id)
                && Objects.equals(description, device.description)
                && Objects.equals(address, device.address)
                && Objects.equals(maxHourlyEnergyConsumption, device.maxHourlyEnergyConsumption)
                && Objects.equals(getUserId(), device.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, address, maxHourlyEnergyConsumption, getUserId());
    }
}
