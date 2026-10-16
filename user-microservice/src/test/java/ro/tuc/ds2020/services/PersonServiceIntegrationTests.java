package ro.tuc.ds2020.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ro.tuc.ds2020.Ds2020TestConfig;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/test-sql/create.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/test-sql/delete.sql")
public class PersonServiceIntegrationTests extends Ds2020TestConfig {

    @Autowired
    private PersonService personService;

    @Test
    public void testGetCorrect() {
        List<PersonDTO> personDTOList = personService.findPersons();
        assertEquals(1, personDTOList.size(), "Test Insert Person");
    }

    @Test
    public void testInsertCorrectWithGetById() {
        PersonDetailsDTO p = new PersonDetailsDTO("John", "Somewhere Else street", 22);
        UUID insertedID = personService.insert(p);

        PersonDTO insertedPerson = new PersonDTO(insertedID, p.getName(), p.getAge());
        PersonDTO fetchedPerson = personService.findPersonById(insertedID);

        assertEquals(insertedPerson, fetchedPerson, "Test Inserted Person");
    }

    @Test
    public void testInsertCorrectWithGetAll() {
        PersonDetailsDTO p = new PersonDetailsDTO("John", "Somewhere Else street", 22);
        personService.insert(p);

        List<PersonDTO> personDTOList = personService.findPersons();
        assertEquals(2, personDTOList.size(), "Test Inserted Persons");
    }

    @Test
    public void testUpdatePerson() {
        PersonDetailsDTO p = new PersonDetailsDTO("Alice", "Cluj Napoca", 24);
        UUID insertedID = personService.insert(p);

        PersonDetailsDTO updateData = new PersonDetailsDTO("Alice Updated", "Cluj Napoca Str. 2", 25);
        PersonDetailsDTO updatedPerson = personService.update(insertedID, updateData);

        assertEquals("Alice Updated", updatedPerson.getName());
        assertEquals("Cluj Napoca Str. 2", updatedPerson.getAddress());
        assertEquals(25, updatedPerson.getAge());
    }

    @Test
    public void testDeletePerson() {
        PersonDetailsDTO p = new PersonDetailsDTO("Bob", "Bucuresti", 30);
        UUID insertedID = personService.insert(p);

        personService.delete(insertedID);

        List<PersonDTO> list = personService.findPersons();
        assertEquals(1, list.size(), "Initial person remains");
    }
}
