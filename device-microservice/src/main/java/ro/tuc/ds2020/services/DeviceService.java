package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.repositories.DeviceRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<DeviceDTO> findDevices() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public DeviceDetailsDTO findDeviceById(UUID id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toDeviceDetailsDTO(deviceOptional.get());
    }

    public UUID insert(DeviceDetailsDTO deviceDTO) {
        Device device = DeviceBuilder.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was inserted in db", device.getId());
        return device.getId();
    }

    public DeviceDetailsDTO update(UUID id, DeviceDetailsDTO deviceDTO) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db for update", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        Device device = deviceOptional.get();
        device.setDescription(deviceDTO.getDescription());
        device.setAddress(deviceDTO.getAddress());
        device.setMaxHourlyEnergyConsumption(deviceDTO.getMaxHourlyEnergyConsumption());
        device.setUserId(deviceDTO.getUserId());
        Device updatedDevice = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was updated in db", updatedDevice.getId());
        return DeviceBuilder.toDeviceDetailsDTO(updatedDevice);
    }

    public void delete(UUID id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db for deletion", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        deviceRepository.delete(deviceOptional.get());
        LOGGER.debug("Device with id {} was deleted from db", id);
    }

    public List<DeviceDTO> findDevicesByUserId(UUID userId) {
        List<Device> deviceList = deviceRepository.findByUserId(userId);
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteDevicesByUserId(UUID userId) {
        deviceRepository.deleteByUserId(userId);
        LOGGER.debug("Devices for user with id {} were deleted from db", userId);
    }
}
