package com.digi_backpack_api.digiBackpackApi.Dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;
    private String content;

    private Long createdById;
    private String createdByFirstName;
    private String createdByLastName;
    private String createdByRole;
    private Long assignmentId;
    private Long learningMaterialId;
    private Long classroomId;

    private Long recipientStudentId;

    private LocalDateTime createdAt;
}
