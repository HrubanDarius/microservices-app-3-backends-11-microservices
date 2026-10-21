package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.entities.UserReference;
import ro.tuc.ds2020.repositories.UserReferenceRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserReferenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserReferenceService.class);
    private final UserReferenceRepository userReferenceRepository;

    @Autowired
    public UserReferenceService(UserReferenceRepository userReferenceRepository) {
        this.userReferenceRepository = userReferenceRepository;
    }

    @Transactional
    public UUID insert(UUID userId) {
        UserReference userReference = new UserReference(userId);
        userReference = userReferenceRepository.save(userReference);
        LOGGER.debug("UserReference with id {} was inserted in device db", userReference.getId());
        return userReference.getId();
    }

    @Transactional
    public void delete(UUID userId) {
        Optional<UserReference> userReferenceOptional = userReferenceRepository.findById(userId);
        if (!userReferenceOptional.isPresent()) {
            LOGGER.error("UserReference with id {} was not found in device db for deletion", userId);
            throw new ResourceNotFoundException(UserReference.class.getSimpleName() + " with id: " + userId);
        }
        userReferenceRepository.delete(userReferenceOptional.get());
        LOGGER.debug("UserReference with id {} was deleted from device db", userId);
    }
}
