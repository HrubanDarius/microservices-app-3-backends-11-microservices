package ro.tuc.ds2020.seeders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.PersonRepository;
import ro.tuc.ds2020.services.PersonService;

import java.util.UUID;

/**
 * Seeds the demo client into the person table with fixed UUID c11e7100-0000-0000-0000-000000000001.
 * Triggers automatic sync to device-service's user_account table via PersonService.insert().
 */
@Component
public class DatabaseSeeder implements CommandLineRunner {

    public static final UUID DEMO_CLIENT_ID = UUID.fromString("c11e7100-0000-0000-0000-000000000001");
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final PersonRepository personRepository;
    private final PersonService personService;

    @Autowired
    public DatabaseSeeder(PersonRepository personRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.personService = personService;
    }

    @Override
    public void run(String... args) {
        if (personRepository.count() == 0) {
            LOGGER.info("person table is empty. Seeding demo client...");
            PersonDetailsDTO demo = new PersonDetailsDTO(
                    DEMO_CLIENT_ID,
                    "Client Demonstrativ",
                    "Strada Memorandumului 28",
                    25
            );
            try {
                personService.insert(demo);
                LOGGER.info("Seeded demo client with ID {} and synced with device-service", DEMO_CLIENT_ID);
            } catch (Exception ex) {
                LOGGER.warn("Could not sync with device-service during seed, saving locally: {}", ex.getMessage());
                Person p = new Person(DEMO_CLIENT_ID, demo.getName(), demo.getAddress(), demo.getAge());
                personRepository.save(p);
            }
        } else {
            LOGGER.info("person table already contains {} records. Skipping seed.", personRepository.count());
        }
    }
}
