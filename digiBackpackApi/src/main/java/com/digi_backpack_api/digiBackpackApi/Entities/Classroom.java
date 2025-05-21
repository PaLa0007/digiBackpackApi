package com.digi_backpack_api.digiBackpackApi.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., "Math 4A"

    private String grade;

    @ManyToOne
    private School school;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private Teacher teacher;
}
