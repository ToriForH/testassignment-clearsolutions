package com.herchanivska.viktoriia.teatassignment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.herchanivska.viktoriia.teatassignment.model.User;
import lombok.Value;

import java.time.LocalDate;

@Value
public class UserResponse {
    Long id;
    private String email;
    private String firstName;
    private String lastName;

    private LocalDate birthDate; //field type could be changed due to application requirements
    private String address;
    private String phoneNumber;
    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.birthDate = user.getBirthDate();
        this.address = user.getAddress();
        this.phoneNumber = user.getPhoneNumber();
    }
}
