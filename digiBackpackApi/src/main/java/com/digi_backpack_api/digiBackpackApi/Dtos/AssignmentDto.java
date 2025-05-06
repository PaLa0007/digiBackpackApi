package com.digi_backpack_api.digiBackpackApi.Dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AssignmentDto {

    private Long id;

    private String title;

    private String description;

    private LocalDate dueDate;

    private Long createdById;

    private Long classroomId;
}
