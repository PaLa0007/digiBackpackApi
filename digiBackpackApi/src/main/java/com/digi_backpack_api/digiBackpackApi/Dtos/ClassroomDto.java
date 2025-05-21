package com.digi_backpack_api.digiBackpackApi.Dtos;

import lombok.Data;

@Data
public class ClassroomDto {

    private Long id;

    private String name;

    private String grade;

    private Long schoolId;

    private Long subjectId;

    private Long teacherId;
}
