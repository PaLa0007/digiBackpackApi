package com.digi_backpack_api.digiBackpackApi.Dtos;
import jakarta.validation.constraints.NotNull;


import lombok.Data;
//import lombok.EqualsAndHashCode;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class CreateTeacherDto {
    private String username;
    private String email;
    private String password; // only in creation
    private String firstName;
    private String lastName;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate hireDate;
    private String subjectSpecialization;
    private String phoneNumber;
    @NotNull
    private Long schoolId;
}
