package com.digi_backpack_api.digiBackpackApi.Dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class TeacherDto extends UserDto {

    private LocalDate hireDate;

    private String subjectSpecialization;

    private String phoneNumber;
}
