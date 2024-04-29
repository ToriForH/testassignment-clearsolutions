package com.herchanivska.viktoriia.testassignment.service.impl;

import com.herchanivska.viktoriia.testassignment.model.User;
import com.herchanivska.viktoriia.testassignment.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    private static User user1;
    private static User user2;

    @BeforeAll
    static void setupBeforeClass() {
        user1 = new User(null, "email@mail.com", "First-Name",
                "Last", LocalDate.of(1995, 5, 4),
                "Chicago, USA", "123456789");
        user2 = new User(null, "some@email.com", "Jack",
                "Brown", LocalDate.of(1998, 2, 17),
                null, "9876543");
    }

    @BeforeEach
    void setupBeforeMethod() {
        List<User> users = userService.getAll();
        if (users.size() == 0) {
            userService.create(user1);
            userService.create(user2);
        }
    }

    @Test
    @Order(2)
    void create() {
        User expected = new User(null, "mail@some.org", "Hannah",
                "Smith", LocalDate.of(2003, 7, 26),
                null, null);
        User actual = userService.create(expected);
        List<User> all = userService.getAll();
        assertEquals(3, all.size());
        assertEquals(3, actual.getId());
        assertEquals(expected, actual);
    }

    @Test
    @Order(2)
    void updateField() {
        User input = new User();
        input.setId(2L);
        String newFirstName = "David";
        LocalDate newBirthDate = user2.getBirthDate().minusYears(2);
        input.setFirstName(newFirstName);
        input.setBirthDate(newBirthDate);
        User expected = user2;
        String expectedEmail = user2.getEmail();
        String expectedLastName = user2.getLastName();
        String expectedAddress = user2.getAddress();
        String expectedPhoneNumber = user2.getPhoneNumber();
        User actual = null;
        try {
            actual = userService.updateField(input);
        } catch (Exception e) {
            fail("Method throw exception");
        }
        assertEquals(expected, actual);
        assertEquals(expectedEmail, actual.getEmail());
        assertEquals(newFirstName, actual.getFirstName());
        assertEquals(expectedLastName, actual.getLastName());
        assertEquals(newBirthDate, actual.getBirthDate());
        assertEquals(expectedAddress, actual.getAddress());
        assertEquals(expectedPhoneNumber, actual.getPhoneNumber());
    }

    @Test
    @Order(3)
    void update() {
        User expected = new User(2L, "new@mail.ua", "New-Name",
                "Some-Last", LocalDate.of(1997, 8, 9),
                null, null);
        User actual = userService.update(expected);
        assertEquals(expected, actual);
        assertEquals(userService.readById(2), actual);
    }

    @Test
    @Order(3)
    void delete() {
        int prevSize = userService.getAll().size();
        userService.delete(1);
        assertEquals(prevSize - 1, userService.getAll().size());
        assertNull(userService.readById(1));
    }

    @Test
    @Order(2)
    void searchByBirthDate() {
        LocalDate from = LocalDate.of(1998, 10, 2);
        LocalDate to = LocalDate.of(1990, 4, 5);
        List<User> actual = userService.searchByBirthDate(from, to);
        for(User user: actual) {
            assertTrue(user.getBirthDate().isAfter(from) || user.getBirthDate().equals(from));
            assertTrue(user.getBirthDate().isBefore(to) || user.getBirthDate().equals(to));
        }
    }

    @Test
    @Order(2)
    void readById() {
        User expected = user2;
        User actual = userService.readById(2L);
        assertEquals(expected, actual);
    }

    @Test
    @Order(1)
    void getAll() {
        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);
        List<User> actual = userService.getAll();
        assertTrue(actual.containsAll(expected));
        for (User user: actual){
            System.out.println(user);
        }
        assertEquals(expected.size(), actual.size());
    }
}