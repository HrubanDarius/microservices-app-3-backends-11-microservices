package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.dtos.builders.PersonBuilder;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;
    private final DeviceServiceClient deviceServiceClient;

    @Autowired
    public PersonService(PersonRepository personRepository, DeviceServiceClient deviceServiceClient) {
        this.personRepository = personRepository;
        this.deviceServiceClient = deviceServiceClient;
    }

    public List<PersonDTO> findPersons() {
        List<Person> personList = personRepository.findAll();
        return personList.stream()
                .map(PersonBuilder::toPersonDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findPersonById(UUID id) {
        Optional<Person> prosumerOptional = personRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
        return PersonBuilder.toPersonDTO(prosumerOptional.get());
    }

    @Transactional
    public UUID insert(PersonDetailsDTO personDTO) {
        UUID generatedId = personDTO.getId() != null ? personDTO.getId() : UUID.randomUUID();

        LOGGER.info("Step 1: Syncing user creation to device-service with id: {}", generatedId);
        deviceServiceClient.syncUserCreation(generatedId);

        LOGGER.info("Step 3: Saving user to user-db with id: {}", generatedId);
        Person person = PersonBuilder.toEntity(personDTO);
        person.setId(generatedId);
        person = personRepository.save(person);

        LOGGER.debug("Person with id {} was inserted in db", person.getId());
        return person.getId();
    }

    public PersonDetailsDTO update(UUID id, PersonDetailsDTO personDTO) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (!personOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db for update", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
        Person person = personOptional.get();
        person.setName(personDTO.getName());
        person.setAddress(personDTO.getAddress());
        person.setAge(personDTO.getAge());
        Person updatedPerson = personRepository.save(person);
        LOGGER.debug("Person with id {} was updated in db", updatedPerson.getId());
        return PersonBuilder.toPersonDetailsDTO(updatedPerson);
    }

    @Transactional
    public void delete(UUID id) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (!personOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db for deletion", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }

        LOGGER.info("Step 1: Syncing user deletion to device-service for userId: {}", id);
        deviceServiceClient.syncUserDeletion(id);

        LOGGER.info("Step 2: Deleting user from user-db with id: {}", id);
        personRepository.delete(personOptional.get());
        LOGGER.debug("Person with id {} was deleted from db", id);
    }
}
