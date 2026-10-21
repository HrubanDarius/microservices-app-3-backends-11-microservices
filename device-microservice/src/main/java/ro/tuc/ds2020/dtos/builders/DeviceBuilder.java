package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.entities.Device;

import java.util.UUID;

public class DeviceBuilder {

    private DeviceBuilder() {
    }

    public static DeviceDTO toDeviceDTO(Device device) {
        UUID userId = device.getUser() != null ? device.getUser().getId() : device.getUserId();
        return new DeviceDTO(
                device.getId(),
                device.getDescription(),
                device.getMaxHourlyEnergyConsumption(),
                userId
        );
    }

    public static DeviceDetailsDTO toDeviceDetailsDTO(Device device) {
        UUID userId = device.getUser() != null ? device.getUser().getId() : device.getUserId();
        return new DeviceDetailsDTO(
                device.getId(),
                device.getDescription(),
                device.getAddress(),
                device.getMaxHourlyEnergyConsumption(),
                userId
        );
    }

    public static Device toEntity(DeviceDetailsDTO deviceDetailsDTO) {
        Device device = new Device(
                deviceDetailsDTO.getDescription(),
                deviceDetailsDTO.getAddress(),
                deviceDetailsDTO.getMaxHourlyEnergyConsumption(),
                deviceDetailsDTO.getUserId()
        );
        if (deviceDetailsDTO.getId() != null) {
            device.setId(deviceDetailsDTO.getId());
        }
        return device;
    }
}
