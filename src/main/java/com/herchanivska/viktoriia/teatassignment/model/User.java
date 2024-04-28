package com.herchanivska.viktoriia.teatassignment.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class User {
    Long id;

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
}
