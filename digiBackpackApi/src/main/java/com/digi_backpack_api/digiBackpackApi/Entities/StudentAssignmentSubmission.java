package com.digi_backpack_api.digiBackpackApi.Entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

    @Entity
    @Getter
    @Setter
    public class StudentAssignmentSubmission {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String fileName;

        private String filePath;

        private LocalDate submittedAt;

        @ManyToOne
        private Student student;

        @ManyToOne
        private Assignment assignment;
    }
