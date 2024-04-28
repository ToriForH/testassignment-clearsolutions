package com.herchanivska.viktoriia.teatassignment.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class User {
    Long id;

    @NotNull
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Pattern(regexp = "[A-Z][a-z]+(-[A-Z][a-z]+)?")
    private String firstName;

    @NotNull
    @NotBlank
    @Pattern(regexp = "[A-Z][a-z]+(-[A-Z][a-z]+)?")
    private String lastName;

    @NotNull
    @Past
    private Instant birthDate; //field type could be changed due to application requirements

    private String address;

    private String phoneNumber;
}
