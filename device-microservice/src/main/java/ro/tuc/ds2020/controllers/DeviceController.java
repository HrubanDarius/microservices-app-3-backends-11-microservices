package ro.tuc.ds2020.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

/**
 * REST controller exposing CRUD endpoints for Device management.
 * Base path: /device (accessible via /device-service/device through the API gateway).
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/device")
@Tag(name = "Device", description = "Operatii CRUD pentru gestionarea dispozitivelor energetice") //pt swagger
@SuppressWarnings("checkstyle:DesignForExtension")
public class DeviceController {

    private final DeviceService deviceService;

    /**
     * Injects the device service.
     *
     * @param deviceService the device service
     */
    @Autowired
    public DeviceController(final DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * Returns all devices stored in the database.
     *
     * @return list of {@link DeviceDTO} (HTTP 200 OK)
     */
    @Operation(summary = "Listeaza toate dispozitivele",
            description = "Returneaza lista completa de dispozitive din baza de date.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista returnata cu succes")
    })
    @GetMapping
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        final List<DeviceDTO> dtos = deviceService.findDevices();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Returns full details for a single device by UUID.
     *
     * @param id the UUID of the device
     * @return {@link DeviceDetailsDTO} (HTTP 200 OK)
     */
    @Operation(summary = "Obtine detalii dispozitiv dupa ID",
            description = "Returneaza datele complete ale dispozitivului identificat prin UUID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dispozitiv gasit"),
        @ApiResponse(responseCode = "404", description = "Dispozitiv negasit")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDetailsDTO> getDeviceById(@PathVariable("id") final UUID id) {
        final DeviceDetailsDTO dto = deviceService.findDeviceById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * Returns all devices assigned to a specific user (client).
     *
     * @param userId the UUID of the user/client
     * @return list of {@link DeviceDTO} for that user (HTTP 200 OK)
     */
    @Operation(summary = "Listeaza dispozitivele unui utilizator",
            description = "Returneaza toate dispozitivele alocate utilizatorului identificat prin UUID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista returnata cu succes"),
        @ApiResponse(responseCode = "404", description = "Utilizatorul nu a fost gasit")
    })
    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<DeviceDTO>> getDevicesByUserId(@PathVariable("userId") final UUID userId) {
        final List<DeviceDTO> dtos = deviceService.findDevicesByUserId(userId);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Creates a new device.
     *
     * @param deviceDTO device details payload
     * @return UUID of the newly created device (HTTP 201 Created)
     */
    @Operation(summary = "Adauga un dispozitiv nou",
            description = "Insereaza un dispozitiv nou in baza de date si returneaza UUID-ul generat.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Dispozitiv creat cu succes"),
        @ApiResponse(responseCode = "400", description = "Date invalide in request body")
    })
    @PostMapping
    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody final DeviceDetailsDTO deviceDTO) {
        final UUID deviceId = deviceService.insert(deviceDTO);
        return new ResponseEntity<>(deviceId, HttpStatus.CREATED);
    }

    /**
     * Updates an existing device by UUID.
     *
     * @param id        the UUID of the device to update
     * @param deviceDTO updated device details payload
     * @return updated {@link DeviceDetailsDTO} (HTTP 200 OK)
     */
    @Operation(summary = "Actualizeaza un dispozitiv",
            description = "Modifica datele dispozitivului identificat prin UUID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dispozitiv actualizat cu succes"),
        @ApiResponse(responseCode = "404", description = "Dispozitiv negasit"),
        @ApiResponse(responseCode = "400", description = "Date invalide in request body")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<DeviceDetailsDTO> updateDevice(
            @PathVariable("id") final UUID id,
            @Valid @RequestBody final DeviceDetailsDTO deviceDTO) {
        final DeviceDetailsDTO updatedDevice = deviceService.update(id, deviceDTO);
        return new ResponseEntity<>(updatedDevice, HttpStatus.OK);
    }

    /**
     * Deletes a device by UUID.
     *
     * @param id the UUID of the device to delete
     * @return empty response (HTTP 204 No Content)
     */
    @Operation(summary = "Sterge un dispozitiv",
            description = "Elimina dispozitivul identificat prin UUID din baza de date.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Dispozitiv sters cu succes"),
        @ApiResponse(responseCode = "404", description = "Dispozitiv negasit")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable("id") final UUID id) {
        deviceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes all devices assigned to a specific user.
     *
     * @param userId the UUID of the user whose devices should be deleted
     * @return empty response (HTTP 204 No Content)
     */
    @Operation(summary = "Sterge toate dispozitivele unui utilizator",
            description = "Elimina toate dispozitivele alocate utilizatorului identificat prin UUID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Dispozitive sterse cu succes")
    })
    @DeleteMapping(value = "/user/{userId}")
    public ResponseEntity<Void> deleteDevicesByUserId(@PathVariable("userId") final UUID userId) {
        deviceService.deleteDevicesByUserId(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
