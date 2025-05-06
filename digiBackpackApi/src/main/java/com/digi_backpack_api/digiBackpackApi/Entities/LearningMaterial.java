package com.digi_backpack_api.digiBackpackApi.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class LearningMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String fileUrl;

    @ManyToOne
    private Teacher uploadedBy;

    @ManyToOne
    private Classroom classroom;
}
