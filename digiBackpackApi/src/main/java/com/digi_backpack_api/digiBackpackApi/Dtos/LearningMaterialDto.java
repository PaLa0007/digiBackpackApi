package com.digi_backpack_api.digiBackpackApi.Dtos;

import lombok.Data;

@Data
public class LearningMaterialDto {

    private Long id;

    private String title;

    private String description;

    private String fileUrl;

    private Long uploadedById;

    private Long classroomId;
}
