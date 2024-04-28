package com.herchanivska.viktoriia.teatassignment.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {

    private static User validUser;

    @BeforeAll
    static void init() {
        validUser = new User();
    }
    @BeforeEach
    void initFields() {
        validUser.setEmail("valid@mail.com");
        validUser.setFirstName("Valid-Name");
        validUser.setLastName("Valid");
        validUser.setBirthDate(LocalDate.now().minus(5, ChronoUnit.YEARS));
        validUser.setAddress("Any address");
        validUser.setPhoneNumber("1234567890");
    }

    @Test
    void userValid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertEquals(0, violations.size());
    }

    @Test
    void userValidNoAddress() {
        validUser.setAddress(null);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertEquals(0, violations.size());
    }

    @Test
    void userValidNoPhone() {
        validUser.setPhoneNumber(null);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertEquals(0, violations.size());
    }

    @Test
    void constraintViolationNullEmail() {
        User invalid = validUser;
        invalid.setEmail(null);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(invalid);
        assertEquals(1, violations.size());
        assertNull(violations.iterator().next().getInvalidValue());
        assertEquals("Email can not be null", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidEmail")
    void constraintViolationInvalidEmail(String input, String errorValue) {
        User invalid = validUser;
        invalid.setEmail(input);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(invalid);
        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
        assertEquals("Please provide valid email", violations.iterator().next().getMessage());
    }

    private static Stream<Arguments> provideInvalidEmail() {
        return Stream.of(
                Arguments.of("invalid", "invalid"),
                Arguments.of("", ""),
                Arguments.of("invalid@mail", "invalid@mail"),
                Arguments.of("not@valid.email", "not@valid.email")
        );
    }

    @Test
    void constraintViolationNullFirstName() {
        User invalid = validUser;
        invalid.setFirstName(null);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(invalid);
        assertEquals(1, violations.size());
        assertNull(violations.iterator().next().getInvalidValue());
        assertEquals("First name can not be null", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidFirstName")
    void constraintViolationInvalidFirstName(String input, String errorValue) {
        User invalid = validUser;
        invalid.setFirstName(input);
        String expectedMessage = "First name should start with capital letter " +
                "and be followed by lowercase letters. If there are second part on name it should be separated " +
                "by hyphen and also should start with capital letter and be followed by lowercase letters";

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(invalid);
        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
        assertEquals(expectedMessage, violations.iterator().next().getMessage());
    }

    private static Stream<Arguments> provideInvalidFirstName() {
        return Stream.of(
                Arguments.of("invalid", "invalid"),
                Arguments.of("", ""),
                Arguments.of("invAlid", "invAlid"),
                Arguments.of("Invalid-name", "Invalid-name"),
                Arguments.of("In-Valid-", "In-Valid-"),
                Arguments.of("INVALID", "INVALID")
        );
    }

    @Test
    void constraintViolationNullLastName() {
        User invalid = validUser;
        invalid.setLastName(null);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(invalid);
        assertEquals(1, violations.size());
        assertNull(violations.iterator().next().getInvalidValue());
        assertEquals("Last name can not be null", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidLastName")
    void constraintViolationInvalidLastName(String input, String errorValue) {
        User invalid = validUser;
        invalid.setLastName(input);
        String expectedMessage = "Last name should start with capital letter " +
                "and be followed by lowercase letters. If there are second part on name it should be separated " +
                "by hyphen and also should start with capital letter and be followed by lowercase letters";

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(invalid);
        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
        assertEquals(expectedMessage, violations.iterator().next().getMessage());
    }

    private static Stream<Arguments> provideInvalidLastName() {
        return Stream.of(
                Arguments.of("invalid", "invalid"),
                Arguments.of("", ""),
                Arguments.of("invAlid", "invAlid"),
                Arguments.of("Invalid-name", "Invalid-name"),
                Arguments.of("In-Valid-", "In-Valid-"),
                Arguments.of("INVALID", "INVALID")
        );
    }

    @Test
    void constraintViolationNullBirthDate() {
        User invalid = validUser;
        invalid.setBirthDate(null);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(invalid);
        assertEquals(1, violations.size());
        assertNull(violations.iterator().next().getInvalidValue());
        assertEquals("Birth date can not be null", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidBirthDate")
    void constraintViolationInvalidBirthDate(LocalDate input, LocalDate errorValue) {
        User invalid = validUser;
        invalid.setBirthDate(input);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(invalid);
        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
        assertEquals("Birth date should be before current date", violations.iterator().next().getMessage());
    }

    private static Stream<Arguments> provideInvalidBirthDate() {
        return Stream.of(
                Arguments.of(LocalDate.now(), LocalDate.now()),
                Arguments.of(LocalDate.now().plus(1, ChronoUnit.DAYS), LocalDate.now().plus(1, ChronoUnit.DAYS)),
                Arguments.of(LocalDate.now().plus(1, ChronoUnit.MONTHS), LocalDate.now().plus(1, ChronoUnit.MONTHS)),
                Arguments.of(LocalDate.now().plus(1, ChronoUnit.YEARS), LocalDate.now().plus(1, ChronoUnit.YEARS))
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPhoneNumber")
    void constraintViolationInvalidPhoneNumber(String input, String errorValue, String errorMessage) {
        User invalid = validUser;
        invalid.setPhoneNumber(input);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(invalid);
        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
        assertEquals(errorMessage, violations.iterator().next().getMessage());
    }

    private static Stream<Arguments> provideInvalidPhoneNumber() {
        return Stream.of(
                Arguments.of("1nval1dN4mber", "1nval1dN4mber", "Phone number should contain only digits"),
                Arguments.of("12345y", "12345y", "Phone number should contain only digits"),
                Arguments.of("123", "123", "Phone number can not be less than 4 digits or more than 13 digits"),
                Arguments.of("12345678901234", "12345678901234",  "Phone number can not be less than 4 digits or more than 13 digits")
        );
    }
}