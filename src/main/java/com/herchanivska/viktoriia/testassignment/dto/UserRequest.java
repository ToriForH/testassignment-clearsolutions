package com.herchanivska.viktoriia.testassignment.dto;

import com.herchanivska.viktoriia.testassignment.model.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotNull(message = "Email can not be null")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Please provide valid email")
    private String email;

    @NotNull(message = "First name can not be null")
    @Pattern(regexp = "[A-Z][a-z]+(-[A-Z][a-z]+)?", message = "First name should start with capital letter " +
            "and be followed by lowercase letters. If there are second part on name it should be separated " +
            "by hyphen and also should start with capital letter and be followed by lowercase letters")
    private String firstName;

    @NotNull(message = "Last name can not be null")
    @Pattern(regexp = "[A-Z][a-z]+(-[A-Z][a-z]+)?", message = "Last name should start with capital letter " +
            "and be followed by lowercase letters. If there are second part on name it should be separated " +
            "by hyphen and also should start with capital letter and be followed by lowercase letters")
    private String lastName;

    @NotNull(message = "Birth date can not be null")
    @Past(message = "Birth date should be before current date")
    private LocalDate birthDate; //field type could be changed due to application requirements

    private String address;

    @Pattern(regexp = "\\d+", message = "Phone number should contain only digits")
    @Size(min = 4, max = 13, message = "Phone number can not be less than 4 digits or more than 13 digits")
    private String phoneNumber;

    public User getUserEntity() {
        return new User(null, email, firstName, lastName, birthDate, address, phoneNumber);
    }
}
