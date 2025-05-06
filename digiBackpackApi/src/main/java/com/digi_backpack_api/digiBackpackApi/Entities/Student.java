package com.digi_backpack_api.digiBackpackApi.Entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Student extends User {

    private String gradeLevel;

    private String parentName;

    private String parentPhone;
}
