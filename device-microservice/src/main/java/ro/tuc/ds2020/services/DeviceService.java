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
import ro.tuc.ds2020.entities.UserReference;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.UserReferenceRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;
    private final UserReferenceRepository userReferenceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, UserReferenceRepository userReferenceRepository) {
        this.deviceRepository = deviceRepository;
        this.userReferenceRepository = userReferenceRepository;
    }

    public List<DeviceDTO> findDevices() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public DeviceDetailsDTO findDeviceById(UUID id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (deviceOptional.isEmpty()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toDeviceDetailsDTO(deviceOptional.get());
    }

    public UUID insert(DeviceDetailsDTO deviceDTO) {
        UserReference userReference = null;
        if (deviceDTO.getUserId() != null) {
            Optional<UserReference> userReferenceOptional = userReferenceRepository.findById(deviceDTO.getUserId());
            if (userReferenceOptional.isEmpty()) {
                LOGGER.error("User with id {} was not found in user_account shadow table for device insertion", deviceDTO.getUserId());
                throw new ResourceNotFoundException("User not found with id: " + deviceDTO.getUserId());
            }
            userReference = userReferenceOptional.get();
        }

        Device device = DeviceBuilder.toEntity(deviceDTO);
        device.setUser(userReference);
        device = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was inserted in db", device.getId());
        return device.getId();
    }

    public DeviceDetailsDTO update(UUID id, DeviceDetailsDTO deviceDTO) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (deviceOptional.isEmpty()) {
            LOGGER.error("Device with id {} was not found in db for update", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }

        UserReference userReference = null;
        if (deviceDTO.getUserId() != null) {
            Optional<UserReference> userReferenceOptional = userReferenceRepository.findById(deviceDTO.getUserId());
            if (userReferenceOptional.isEmpty()) {
                LOGGER.error("User with id {} was not found in user_account shadow table for device update", deviceDTO.getUserId());
                throw new ResourceNotFoundException("User not found with id: " + deviceDTO.getUserId());
            }
            userReference = userReferenceOptional.get();
        }

        Device device = deviceOptional.get();
        device.setDescription(deviceDTO.getDescription());
        device.setAddress(deviceDTO.getAddress());
        device.setMaxHourlyEnergyConsumption(deviceDTO.getMaxHourlyEnergyConsumption());
        device.setUser(userReference);

        Device updatedDevice = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was updated in db", updatedDevice.getId());
        return DeviceBuilder.toDeviceDetailsDTO(updatedDevice);
    }

    public void delete(UUID id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (deviceOptional.isEmpty()) {
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
