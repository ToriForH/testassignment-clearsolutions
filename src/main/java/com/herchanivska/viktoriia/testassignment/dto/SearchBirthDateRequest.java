package com.herchanivska.viktoriia.testassignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchBirthDateRequest {
    LocalDate from;
    LocalDate to;
}
