package com.herchanivska.viktoriia.teatassignment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.herchanivska.viktoriia.teatassignment.model.User;
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
    @NotNull
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Please provide valid email")
    private String email;

    @NotNull
    @NotBlank
    @Pattern(regexp = "[A-Z][a-z]+(-[A-Z][a-z]+)?", message = "First name should start with capital letter " +
            "and be followed by lowercase letters. If there are second part on name it should be separated " +
            "by hyphen and also should start with capital letter and be followed by lowercase letters")
    private String firstName;

    @NotNull
    @NotBlank
    @Pattern(regexp = "[A-Z][a-z]+(-[A-Z][a-z]+)?", message = "First name should start with capital letter " +
            "and be followed by lowercase letters. If there are second part on name it should be separated " +
            "by hyphen and also should start with capital letter and be followed by lowercase letters")
    private String lastName;

    @NotNull
    @Past(message = "Birth date should be before current date")
    private LocalDate birthDate; //field type could be changed due to application requirements

    private String address;

    private String phoneNumber;

    public User getUserEntity() {
        return new User(null, email, firstName, lastName, birthDate, address, phoneNumber);
    }
}
