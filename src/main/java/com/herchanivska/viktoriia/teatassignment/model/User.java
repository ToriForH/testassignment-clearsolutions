package com.herchanivska.viktoriia.teatassignment.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
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
    private Instant birthDate;

    private String address;

    private String phoneNumber;
}
