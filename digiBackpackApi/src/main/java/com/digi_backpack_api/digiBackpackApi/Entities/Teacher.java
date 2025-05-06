package com.digi_backpack_api.digiBackpackApi.Entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Teacher extends User {

    private LocalDate hireDate;

    private String subjectSpecialization;

    private String phoneNumber;
}
