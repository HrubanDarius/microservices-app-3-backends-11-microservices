package ro.tuc.ds2020.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ro.tuc.ds2020.Ds2020TestConfig;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.entities.UserReference;
import ro.tuc.ds2020.repositories.UserReferenceRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/test-sql/create.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/test-sql/delete.sql")
public class DeviceServiceIntegrationTests extends Ds2020TestConfig {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserReferenceRepository userReferenceRepository;

    @Test
    public void testGetCorrect() {
        List<DeviceDTO> deviceDTOList = deviceService.findDevices();
        assertEquals(1, deviceDTOList.size(), "Test Initial Devices in DB");
    }

    @Test
    public void testInsertCorrectWithGetById() {
        DeviceDetailsDTO d = new DeviceDetailsDTO("Sensor Living Room", "Observatorului 34", 18.5);
        UUID insertedID = deviceService.insert(d);
        assertNotNull(insertedID);

        DeviceDetailsDTO fetchedDevice = deviceService.findDeviceById(insertedID);
        assertEquals(d.getDescription(), fetchedDevice.getDescription());
        assertEquals(d.getAddress(), fetchedDevice.getAddress());
        assertEquals(d.getMaxHourlyEnergyConsumption(), fetchedDevice.getMaxHourlyEnergyConsumption());
    }

    @Test
    public void testInsertCorrectWithGetAll() {
        DeviceDetailsDTO d = new DeviceDetailsDTO("Kitchen Meter", "Observatorului 34", 22.0);
        deviceService.insert(d);

        List<DeviceDTO> deviceDTOList = deviceService.findDevices();
        assertEquals(2, deviceDTOList.size(), "Test Inserted Devices");
    }

    @Test
    public void testUpdateDevice() {
        DeviceDetailsDTO d = new DeviceDetailsDTO("Smart Meter V1", "Strada Memorandumului 10", 14.0);
        UUID insertedId = deviceService.insert(d);

        UUID userId = UUID.randomUUID();
        userReferenceRepository.save(new UserReference(userId));
        DeviceDetailsDTO updateDTO = new DeviceDetailsDTO("Smart Meter V2", "Strada Memorandumului 12", 20.0, userId);
        DeviceDetailsDTO updated = deviceService.update(insertedId, updateDTO);

        assertEquals("Smart Meter V2", updated.getDescription());
        assertEquals("Strada Memorandumului 12", updated.getAddress());
        assertEquals(20.0, updated.getMaxHourlyEnergyConsumption());
        assertEquals(userId, updated.getUserId());
    }

    @Test
    public void testFindByUserIdAndDeletion() {
        UUID userId = UUID.randomUUID();
        userReferenceRepository.save(new UserReference(userId));
        DeviceDetailsDTO d1 = new DeviceDetailsDTO("Device 1", "Strada 1", 10.0, userId);
        DeviceDetailsDTO d2 = new DeviceDetailsDTO("Device 2", "Strada 2", 15.0, userId);
        deviceService.insert(d1);
        deviceService.insert(d2);

        List<DeviceDTO> userDevices = deviceService.findDevicesByUserId(userId);
        assertEquals(2, userDevices.size(), "User should have 2 devices");

        deviceService.deleteDevicesByUserId(userId);
        List<DeviceDTO> userDevicesAfterDelete = deviceService.findDevicesByUserId(userId);
        assertEquals(0, userDevicesAfterDelete.size(), "User should have 0 devices after deletion");
    }

    @Test
    public void testDeleteDeviceById() {
        DeviceDetailsDTO d = new DeviceDetailsDTO("Device to delete", "Strada X", 5.0);
        UUID insertedId = deviceService.insert(d);

        deviceService.delete(insertedId);

        List<DeviceDTO> allDevices = deviceService.findDevices();
        assertEquals(1, allDevices.size(), "Only the initial device should remain");
    }

    @Test
    public void testInsertDeviceFailsWhenUserNotFound() {
        UUID nonExistentUserId = UUID.randomUUID();
        DeviceDetailsDTO d = new DeviceDetailsDTO("Device Fail", "Strada Fail", 10.0, nonExistentUserId);
        assertThrows(ResourceNotFoundException.class, () -> deviceService.insert(d));
    }

    @Test
    public void testUpdateDeviceFailsWhenUserNotFound() {
        DeviceDetailsDTO d = new DeviceDetailsDTO("Smart Meter V1", "Strada Memorandumului 10", 14.0);
        UUID insertedId = deviceService.insert(d);

        UUID nonExistentUserId = UUID.randomUUID();
        DeviceDetailsDTO updateDTO = new DeviceDetailsDTO("Smart Meter V2", "Strada Memorandumului 12", 20.0, nonExistentUserId);
        assertThrows(ResourceNotFoundException.class, () -> deviceService.update(insertedId, updateDTO));
    }
}
