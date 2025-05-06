package com.digi_backpack_api.digiBackpackApi.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private Assignment assignment;

    @ManyToOne
    private LearningMaterial learningMaterial;
}
