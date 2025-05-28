package com.digi_backpack_api.digiBackpackApi.Dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentSubmissionDto {
    private Long id;
    private String fileName;
    private String filePath;
    private LocalDate submittedAt;
    private Long studentId;
    private Long assignmentId;
}
