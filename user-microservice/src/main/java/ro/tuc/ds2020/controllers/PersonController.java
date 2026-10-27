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
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.services.PersonService;

import java.util.List;
import java.util.UUID;

/**
 * REST controller exposing CRUD endpoints for Person (User) management.
 * Base path: /person (accessible via /user-service/person through the API gateway).
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/person")
@Tag(name = "Person", description = "Operatii CRUD pentru gestionarea utilizatorilor (persoane)")
@SuppressWarnings("checkstyle:DesignForExtension")
public class PersonController {

    private final PersonService personService;

    /**
     * Injects the person service.
     *
     * @param personService the person service
     */
    @Autowired
    public PersonController(final PersonService personService) {
        this.personService = personService;
    }

    /**
     * Returns all persons stored in the database.
     *
     * @return list of {@link PersonDTO} (HTTP 200 OK)
     */
    @Operation(summary = "Listeaza toti utilizatorii",
            description = "Returneaza lista completa de persoane din baza de date.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista returnata cu succes")
    })
    @GetMapping
    public ResponseEntity<List<PersonDTO>> getPersons() {
        final List<PersonDTO> dtos = personService.findPersons();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Creates a new person.
     *
     * @param personDTO person details payload
     * @return UUID of the newly created person (HTTP 201 Created)
     */
    @Operation(summary = "Creeaza un utilizator nou",
            description = "Insereaza o persoana noua in baza de date si returneaza UUID-ul generat.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Persoana creata cu succes"),
        @ApiResponse(responseCode = "400", description = "Date invalide in request body")
    })
    @PostMapping
    public ResponseEntity<UUID> insertProsumer(@Valid @RequestBody final PersonDetailsDTO personDTO) {
        final UUID personID = personService.insert(personDTO);
        return new ResponseEntity<>(personID, HttpStatus.CREATED);
    }

    /**
     * Returns full details for a single person by UUID.
     *
     * @param personId the UUID of the person
     * @return {@link PersonDetailsDTO} (HTTP 200 OK)
     */
    @Operation(summary = "Obtine detalii utilizator dupa ID",
            description = "Returneaza datele complete ale persoanei identificate prin UUID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Persoana gasita"),
        @ApiResponse(responseCode = "404", description = "Persoana nu a fost gasita")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonDetailsDTO> getPerson(@PathVariable("id") final UUID personId) {
        final PersonDetailsDTO dto = personService.findPersonById(personId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * Updates an existing person by UUID.
     *
     * @param personId  the UUID of the person to update
     * @param personDTO updated person details payload
     * @return updated {@link PersonDetailsDTO} (HTTP 200 OK)
     */
    @Operation(summary = "Actualizeaza un utilizator",
            description = "Modifica datele persoanei identificate prin UUID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Persoana actualizata cu succes"),
        @ApiResponse(responseCode = "404", description = "Persoana nu a fost gasita"),
        @ApiResponse(responseCode = "400", description = "Date invalide in request body")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<PersonDetailsDTO> updatePerson(
            @PathVariable("id") final UUID personId,
            @Valid @RequestBody final PersonDetailsDTO personDTO) {
        final PersonDetailsDTO updatedPerson = personService.update(personId, personDTO);
        return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
    }

    /**
     * Deletes a person by UUID.
     *
     * @param personId the UUID of the person to delete
     * @return empty response (HTTP 204 No Content)
     */
    @Operation(summary = "Sterge un utilizator",
            description = "Elimina persoana identificata prin UUID din baza de date.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Persoana stearsa cu succes"),
        @ApiResponse(responseCode = "404", description = "Persoana nu a fost gasita")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable("id") final UUID personId) {
        personService.delete(personId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
