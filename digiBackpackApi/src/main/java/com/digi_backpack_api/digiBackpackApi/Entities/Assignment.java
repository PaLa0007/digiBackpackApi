package com.digi_backpack_api.digiBackpackApi.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private LocalDate dueDate;

    @ManyToOne
    private Teacher createdBy;

    @ManyToOne
    private Classroom classroom;
}
