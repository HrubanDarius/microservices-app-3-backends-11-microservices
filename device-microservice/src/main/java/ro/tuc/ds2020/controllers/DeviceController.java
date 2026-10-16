package ro.tuc.ds2020.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.services.DeviceService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> dtos = deviceService.findDevices();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDetailsDTO> getDeviceById(@PathVariable("id") UUID id) {
        DeviceDetailsDTO dto = deviceService.findDeviceById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<DeviceDTO>> getDevicesByUserId(@PathVariable("userId") UUID userId) {
        List<DeviceDTO> dtos = deviceService.findDevicesByUserId(userId);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody DeviceDetailsDTO deviceDTO) {
        UUID deviceId = deviceService.insert(deviceDTO);
        return new ResponseEntity<>(deviceId, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<DeviceDetailsDTO> updateDevice(@PathVariable("id") UUID id,
                                                         @Valid @RequestBody DeviceDetailsDTO deviceDTO) {
        DeviceDetailsDTO updatedDevice = deviceService.update(id, deviceDTO);
        return new ResponseEntity<>(updatedDevice, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable("id") UUID id) {
        deviceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/user/{userId}")
    public ResponseEntity<Void> deleteDevicesByUserId(@PathVariable("userId") UUID userId) {
        deviceService.deleteDevicesByUserId(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
