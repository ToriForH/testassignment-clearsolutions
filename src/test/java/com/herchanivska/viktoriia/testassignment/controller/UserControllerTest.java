package com.herchanivska.viktoriia.testassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herchanivska.viktoriia.testassignment.Application;
import com.herchanivska.viktoriia.testassignment.dto.SearchBirthDateRequest;
import com.herchanivska.viktoriia.testassignment.dto.UserRequest;
import com.herchanivska.viktoriia.testassignment.model.User;
import com.herchanivska.viktoriia.testassignment.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    static ObjectMapper mapper;

    static UserRequest validUser;

    static UserRequest invalidUser;

    static UserRequest illegalBirthDateUser;

    static User user1;
    static User user2;
    static User user3;

    static LocalDate from;
    static LocalDate to;

    static LocalDate toNotFoundFrom;
    static LocalDate toNotFoundTo;
    @BeforeAll
    static void init() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        validUser = new UserRequest("some@mail.com", "Sam", "Evans",
                LocalDate.of(1990, 11, 15), null, null);
        invalidUser = new UserRequest(null, "Name", "Last-Name",
                LocalDate.of(1990, 11, 15), null, null);
        illegalBirthDateUser = new UserRequest("email@one.com", "Martha", "Smith",
                LocalDate.now().minusYears(5), null, null);
        user1 = validUser.getUserEntity();
        user1.setId(1L);
        user2 = new User(2L, "second@email.ua", "Sarah", "Brown",
                LocalDate.of(1989, 1, 13), "London, UK", "123456789");
        user3 = new User(3L, "anothe@mail.org", "Tom", "Johnson",
                LocalDate.of(1992, 7, 4), "Chicago, USA", null);

        from = LocalDate.of(1985, 10, 5);
        to = LocalDate.of(1991, 5, 2);

        toNotFoundFrom = LocalDate.of(2005, 10, 5);
        toNotFoundTo = LocalDate.of(2010, 5, 2);
    }

    @BeforeEach
    void setMocks() {
        Mockito.when(userService.readById(1)).thenReturn(user1);
        Mockito.when(userService.readById(2)).thenReturn(user2);
        Mockito.when(userService.readById(3)).thenReturn(user3);
        Mockito.when(userService.readById(4)).thenReturn(null);

        List<User> allUsers = new ArrayList<>();
        allUsers.add(user1);
        allUsers.add(user2);
        allUsers.add(user3);
        Mockito.when(userService.getAll()).thenReturn(allUsers);

        List<User> existRange = new ArrayList<>();
        existRange.add(user1);
        existRange.add(user2);
        Mockito.when(userService.searchByBirthDate(from, to)).thenReturn(existRange);
        Mockito.when(userService.searchByBirthDate(toNotFoundFrom, toNotFoundTo)).thenReturn(new ArrayList<>());

    }

    @Test
    void createValidUser() throws Exception {
        Mockito.when(userService.create(validUser.getUserEntity())).thenReturn(user1);
        String requestBody = mapper.writeValueAsString(validUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("location", "http://localhost/api/users/1"))
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id")
                                .value(user1.getId())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.email")
                                .value(user1.getEmail())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.firstName")
                                .value(user1.getFirstName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.lastName")
                                .value(user1.getLastName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.birthDate")
                                .value(user1.getBirthDate().toString())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.address")
                                .value(user1.getAddress())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.phoneNumber")
                                .value(user1.getPhoneNumber())
                );
    }

    @Test
    void createInvalidUser() throws Exception {
        String requestBody = mapper.writeValueAsString(invalidUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createInvalidBirthDate() throws Exception {
        String requestBody = mapper.writeValueAsString(illegalBirthDateUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateSomeFieldsValidUser() throws Exception {
        String newFirstName = "David";
        String newAddress = "Kyiv, Ukraine";
        UserRequest requestToMock = new UserRequest(null, newFirstName, null, null, newAddress, null);
        User userToMock = requestToMock.getUserEntity();
        userToMock.setId(1L);
        User toMockReturn = new User(1L, user1.getEmail(), newFirstName, user1.getLastName(),
                user1.getBirthDate(), newAddress, user1.getPhoneNumber());
        Mockito.when(userService.updateField(userToMock)).thenReturn(toMockReturn);

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("firstName", newFirstName);
        requestParams.put("address", newAddress);
        String requestBody = mapper.writeValueAsString(requestParams);
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id")
                                .value(toMockReturn.getId())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.email")
                                .value(toMockReturn.getEmail())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.firstName")
                                .value(toMockReturn.getFirstName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.lastName")
                                .value(toMockReturn.getLastName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.birthDate")
                                .value(toMockReturn.getBirthDate().toString())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.address")
                                .value(toMockReturn.getAddress())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.phoneNumber")
                                .value(toMockReturn.getPhoneNumber())
                );
    }

    @Test
    void updateSomeFieldsInvalidUserId() throws Exception {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("firstName", "David");
        requestParams.put("address", "Kyiv, Ukraine");
        String requestBody = mapper.writeValueAsString(requestParams);
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/4")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateSomeFieldIllegalAge() throws Exception {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("firstName", "David");
        requestParams.put("birthDate", illegalBirthDateUser.getBirthDate().toString());
        String requestBody = mapper.writeValueAsString(requestParams);
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateValidUser() throws Exception {
        UserRequest updateUser = new UserRequest("new@mail.org", "Garry", "Turner",
                LocalDate.of(1993, 6,26), null, null);
        User userToMock = updateUser.getUserEntity();
        userToMock.setId(1L);
        User toMockReturn = new User(1L, updateUser.getEmail(), updateUser.getFirstName(), updateUser.getLastName(),
                updateUser.getBirthDate(), updateUser.getAddress(), updateUser.getPhoneNumber());
        Mockito.when(userService.update(userToMock)).thenReturn(toMockReturn);

        String requestBody = mapper.writeValueAsString(updateUser);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id")
                                .value(toMockReturn.getId())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.email")
                                .value(toMockReturn.getEmail())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.firstName")
                                .value(toMockReturn.getFirstName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.lastName")
                                .value(toMockReturn.getLastName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.birthDate")
                                .value(toMockReturn.getBirthDate().toString())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.address")
                                .value(toMockReturn.getAddress())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.phoneNumber")
                                .value(toMockReturn.getAddress())
                );
    }

    @Test
    void updateInvalidInput() throws Exception {
        String requestBody = mapper.writeValueAsString(invalidUser);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateInvalidUserId() throws Exception {
        String requestBody = mapper.writeValueAsString(validUser);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/4")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateUserIllegalAge() throws Exception {
        String requestBody = mapper.writeValueAsString(illegalBirthDateUser);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deleteValidUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteInvalidUserId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/4"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void searchByBirthDate() throws Exception {
        SearchBirthDateRequest range = new SearchBirthDateRequest(from, to);
        String requestBody = mapper.writeValueAsString(range);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/search")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").isMap())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].id")
                                .value(user1.getId())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].email")
                                .value(user1.getEmail())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].firstName")
                                .value(user1.getFirstName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].lastName")
                                .value(user1.getLastName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].birthDate")
                                .value(user1.getBirthDate().toString())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].address")
                                .value(user1.getAddress())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].phoneNumber")
                                .value(user1.getPhoneNumber())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[1].id")
                                .value(user2.getId())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[1].email")
                                .value(user2.getEmail())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[1].firstName")
                                .value(user2.getFirstName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[1].lastName")
                                .value(user2.getLastName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[1].birthDate")
                                .value(user2.getBirthDate().toString())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[1].address")
                                .value(user2.getAddress())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[1].phoneNumber")
                                .value(user2.getPhoneNumber())
                );
    }

    @Test
    void searchByBirthDateInvalidRange() throws Exception {
        LocalDate from = LocalDate.of(2003, 10, 5);
        LocalDate to = LocalDate.of(1998, 5, 2);
        SearchBirthDateRequest range = new SearchBirthDateRequest(from, to);
        String requestBody = mapper.writeValueAsString(range);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/search")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void searchByBirthDateEmpty() throws Exception {
        SearchBirthDateRequest range = new SearchBirthDateRequest(toNotFoundFrom, toNotFoundTo);
        String requestBody = mapper.writeValueAsString(range);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/search")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    void readById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id")
                                .value(user1.getId())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.email")
                                .value(user1.getEmail())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.firstName")
                                .value(user1.getFirstName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.lastName")
                                .value(user1.getLastName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.birthDate")
                                .value(user1.getBirthDate().toString())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.address")
                                .value(user1.getAddress())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.phoneNumber")
                                .value(user1.getPhoneNumber())
                );
    }

    @Test
    void readByIdInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/4"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").isMap())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].id")
                                .value(user1.getId())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].email")
                                .value(user1.getEmail())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].firstName")
                                .value(user1.getFirstName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].lastName")
                                .value(user1.getLastName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].birthDate")
                                .value(user1.getBirthDate().toString())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].address")
                                .value(user1.getAddress())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].phoneNumber")
                                .value(user1.getPhoneNumber())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[1].id")
                                .value(user2.getId())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[1].email")
                                .value(user2.getEmail())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[1].firstName")
                                .value(user2.getFirstName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[1].lastName")
                                .value(user2.getLastName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[1].birthDate")
                                .value(user2.getBirthDate().toString())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[1].address")
                                .value(user2.getAddress())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[1].phoneNumber")
                                .value(user2.getPhoneNumber())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[2].id")
                                .value(user3.getId())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[2].email")
                                .value(user3.getEmail())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[2].firstName")
                                .value(user3.getFirstName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[2].lastName")
                                .value(user3.getLastName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[2].birthDate")
                                .value(user3.getBirthDate().toString())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[2].address")
                                .value(user3.getAddress())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[2].phoneNumber")
                                .value(user3.getPhoneNumber())
                );
    }
}