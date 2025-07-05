package com.digi_backpack_api.digiBackpackApi.Dtos;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class StudentSubmissionDto {
    private Long id;
    private LocalDate submittedAt;
    private Long studentId;
    private Long assignmentId;
    private String description;

    // For multiple file support with fileId + fileName
    private List<StudentSubmissionFileDto> files;

    private String studentFirstName;
    private String studentLastName;
}
