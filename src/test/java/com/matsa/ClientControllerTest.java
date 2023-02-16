package com.matsa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matsa.clients.Client;
import com.matsa.clients.ClientDto;
import com.matsa.clients.ClientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ClientRepository clientRepository;

    @AfterEach
    void clearAfter() {
        clientRepository.deleteAll();
    }

    @Test
    void createClient() throws Exception {
        ClientDto testDto = mockClient();
        createAndAssert(testDto);
    }
    @Test
    void getAllClients() throws Exception{
        ClientDto testDto1 = mockClient();
        ClientDto testDto2 = new ClientDto();
        testDto2.setFirstName("Pedro");
        testDto2.setLastName("Depacos");
        testDto2.setEmail("asdasd@asd.asd");
        testDto2.setPhone("+380689999999");

        createAndAssert(testDto2);
        createAndAssert(testDto1);

        mockMvc.perform(get("/client")
                .param("page", "0")
                .param("size","10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content.[*].firstName",
                        containsInAnyOrder(testDto1.getFirstName(), testDto2.getFirstName())))
                .andExpect(jsonPath("$.content.[*].phone",
                        containsInAnyOrder(testDto1.getPhone(), testDto2.getPhone())));

        mockMvc.perform(get("/client")
                        .param("page", "0")
                        .param("size","10")
                .param("query",testDto1.getFirstName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content.[0].firstName",is(testDto1.getFirstName())));

        mockMvc.perform(get("/client")
                        .param("page", "0")
                        .param("size","10")
                        .param("query",testDto2.getPhone()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content.[0].phone",is(testDto2.getPhone())));
    }

    @Test
    void getClientById() throws Exception {
        ClientDto testDto1 = mockClient();
        Client client = createAndAssert(testDto1);
        mockMvc.perform(get("/client/" + client.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(client.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(client.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(client.getLastName())))
                .andExpect(jsonPath("$.email", is(client.getEmail())))
                .andExpect(jsonPath("$.phone", is(client.getPhone())));
    }
    @Test
    void editClient() throws Exception{
        ClientDto dto = mockClient();
        Client client =createAndAssert(dto);
        dto.setFirstName("Mykola");
        mockMvc.perform(put("/client/" + client.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(client.getId().intValue())))
                .andExpect(jsonPath("$.firstName",is(dto.getFirstName())));
    }

    @Test
    void deleteClient() throws Exception{
        ClientDto dto = mockClient();
        Client client = createAndAssert(dto);
        mockMvc.perform(get("/client/" + client.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/client/" + client.getId()))
                        .andExpect(status().isOk());
        mockMvc.perform(get("/client/" + client.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void findClients() throws Exception {
        ClientDto testClient = mockClient();
        String json = objectMapper.writeValueAsString(testClient);
        mockMvc.perform(post("/client").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(get("/client").param("page", "0").param("size", "1").param("query", "asda"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", empty()))
                .andReturn();
        mockMvc.perform(get("/client").param("page", "0").param("size", "1").param("query", "author"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andReturn();

    }

    @Test
    void createClient_expectBadRequest_wenValidationFailed() throws Exception {
        ClientDto dto = new ClientDto();
        dto.setFirstName(null);
        dto.setLastName("");
        dto.setEmail("wrong email");
        dto.setPhone("98-08909-08-98");
        mockMvc.perform(post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        "firstName is mandatory",
                        "lastName is mandatory",
                        "email must match \"^\\S+@\\S+\\.\\S+$\"",
                        "phone must match \"^\\+380\\d{9}$\"")));




    }

    @Test
    void createClient_expectConflict_whenClientAlreadyExists() throws Exception {
        ClientDto dto = mockClient();
        createAndAssert(dto);
        mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(HttpStatus.CONFLICT.name())))
                .andExpect(jsonPath("$.errors", containsInAnyOrder("Client already exists")));


    }

    @Test
    void getNotExistingClient() throws Exception {
        mockMvc.perform(get("/client/123"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status", equalTo(HttpStatus.NOT_FOUND.name())))
                .andExpect(jsonPath("errors", contains("Client not found")));
    }

    private ClientDto mockClient() {
        ClientDto testClient = new ClientDto();
        testClient.setFirstName("Test name");
        testClient.setLastName("Test last name");
        testClient.setEmail("buba@gmail.com");
        testClient.setPhone("+380998887766");
        return testClient;
    }

    private Client createAndAssert(ClientDto dto) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", not(nullValue())))
                .andExpect(jsonPath("$.firstName", is(dto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(dto.getLastName())))
                .andExpect(jsonPath("$.email", is(dto.getEmail())))
                .andExpect(jsonPath("$.phone", is(dto.getPhone())))
                .andReturn();
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Client.class);
    }
}
