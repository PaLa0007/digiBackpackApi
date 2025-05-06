package com.digi_backpack_api.digiBackpackApi.Dtos;

import lombok.Data;

@Data
public class CommentDto {

    private Long id;

    private String content;

    private Long createdById;

    private Long assignmentId;

    private Long learningMaterialId;
}
