package ro.tuc.ds2020.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.tuc.ds2020.dtos.UserReferenceDTO;
import ro.tuc.ds2020.services.UserReferenceService;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserReferenceController {

    private final UserReferenceService userReferenceService;

    @Autowired
    public UserReferenceController(UserReferenceService userReferenceService) {
        this.userReferenceService = userReferenceService;
    }

    @PostMapping
    public ResponseEntity<UUID> insertUserReference(@Valid @RequestBody UserReferenceDTO userReferenceDTO) {
        UUID userId = userReferenceService.insert(userReferenceDTO.getId());
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUserReference(@PathVariable("id") UUID userId) {
        userReferenceService.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
