package ro.tuc.ds2020.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ro.tuc.ds2020.Ds2020TestConfig;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.services.DeviceService;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeviceControllerUnitTest extends Ds2020TestConfig {

    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeviceService service;

    @Test
    public void insertDeviceTest() throws Exception {
        DeviceDetailsDTO deviceDTO = new DeviceDetailsDTO(
                "Smart Meter Sensor",
                "Strada Memorandumului 28",
                12.5
        );
        when(service.insert(any(DeviceDetailsDTO.class))).thenReturn(UUID.randomUUID());

        mockMvc.perform(post("/device")
                        .content(jsonMapper.writeValueAsString(deviceDTO))
                        .contentType("application/json"))
                .andExpect(status().isCreated());
    }

    @Test
    public void insertDeviceTestFailsDueToNegativeConsumption() throws Exception {
        DeviceDetailsDTO deviceDTO = new DeviceDetailsDTO(
                "Smart Meter Sensor",
                "Strada Memorandumului 28",
                -5.0
        );

        mockMvc.perform(post("/device")
                        .content(jsonMapper.writeValueAsString(deviceDTO))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void insertDeviceTestFailsDueToNullConsumption() throws Exception {
        DeviceDetailsDTO deviceDTO = new DeviceDetailsDTO(
                "Smart Meter Sensor",
                "Strada Memorandumului 28",
                null
        );

        mockMvc.perform(post("/device")
                        .content(jsonMapper.writeValueAsString(deviceDTO))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void insertDeviceTestFailsDueToBlankDescription() throws Exception {
        DeviceDetailsDTO deviceDTO = new DeviceDetailsDTO(
                "",
                "Strada Memorandumului 28",
                10.0
        );

        mockMvc.perform(post("/device")
                        .content(jsonMapper.writeValueAsString(deviceDTO))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void insertDeviceTestFailsDueToBlankAddress() throws Exception {
        DeviceDetailsDTO deviceDTO = new DeviceDetailsDTO(
                "Smart Meter Sensor",
                "",
                10.0
        );

        mockMvc.perform(post("/device")
                        .content(jsonMapper.writeValueAsString(deviceDTO))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getDevicesTest() throws Exception {
        mockMvc.perform(get("/device"))
                .andExpect(status().isOk());
    }

    @Test
    public void getDeviceByIdTest() throws Exception {
        UUID deviceId = UUID.randomUUID();
        when(service.findDeviceById(deviceId)).thenReturn(new DeviceDetailsDTO(
                deviceId,
                "Smart Meter",
                "Strada Cluj 1",
                25.0,
                null
        ));

        mockMvc.perform(get("/device/" + deviceId))
                .andExpect(status().isOk());
    }

    @Test
    public void updateDeviceTest() throws Exception {
        UUID deviceId = UUID.randomUUID();
        DeviceDetailsDTO deviceDTO = new DeviceDetailsDTO(
                deviceId,
                "Updated Smart Meter",
                "Strada Cluj 2",
                30.0,
                UUID.randomUUID()
        );
        when(service.update(eq(deviceId), any(DeviceDetailsDTO.class))).thenReturn(deviceDTO);

        mockMvc.perform(put("/device/" + deviceId)
                        .content(jsonMapper.writeValueAsString(deviceDTO))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteDeviceTest() throws Exception {
        UUID deviceId = UUID.randomUUID();
        mockMvc.perform(delete("/device/" + deviceId))
                .andExpect(status().isNoContent());
    }
}
