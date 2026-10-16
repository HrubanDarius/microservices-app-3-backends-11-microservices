package ro.tuc.ds2020.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ro.tuc.ds2020.Ds2020TestConfig;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.services.PersonService;
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

public class PersonControllerUnitTest extends Ds2020TestConfig {
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService service;

    @Test
    public void insertPersonTest() throws Exception {
        PersonDetailsDTO personDTO = new PersonDetailsDTO("John", "Somewhere Else street", 22);

        mockMvc.perform(post("/person")
                .content(jsonMapper.writeValueAsString(personDTO))
                .contentType("application/json"))
                .andExpect(status().isCreated());
    }

    @Test
    public void insertPersonTestFailsDueToAge() throws Exception {
        PersonDetailsDTO personDTO = new PersonDetailsDTO("John", "Somewhere Else street", 17);

        mockMvc.perform(post("/person")
                .content(jsonMapper.writeValueAsString(personDTO))
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void insertPersonTestFailsDueToNull() throws Exception {
        PersonDetailsDTO personDTO = new PersonDetailsDTO("John", null, 17);

        mockMvc.perform(post("/person")
                .content(jsonMapper.writeValueAsString(personDTO))
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getPersonsTest() throws Exception {
        mockMvc.perform(get("/person"))
                .andExpect(status().isOk());
    }

    @Test
    public void getPersonByIdTest() throws Exception {
        UUID personId = UUID.randomUUID();
        when(service.findPersonById(personId)).thenReturn(new PersonDTO(personId, "John", 22));

        mockMvc.perform(get("/person/" + personId))
                .andExpect(status().isOk());
    }

    @Test
    public void updatePersonTest() throws Exception {
        UUID personId = UUID.randomUUID();
        PersonDetailsDTO personDTO = new PersonDetailsDTO(personId, "John Updated", "Somewhere Else street 2", 25);
        when(service.update(eq(personId), any(PersonDetailsDTO.class))).thenReturn(personDTO);

        mockMvc.perform(put("/person/" + personId)
                        .content(jsonMapper.writeValueAsString(personDTO))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePersonTest() throws Exception {
        UUID personId = UUID.randomUUID();
        mockMvc.perform(delete("/person/" + personId))
                .andExpect(status().isNoContent());
    }
}
