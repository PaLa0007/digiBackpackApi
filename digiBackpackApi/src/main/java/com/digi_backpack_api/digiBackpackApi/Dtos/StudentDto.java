package com.digi_backpack_api.digiBackpackApi.Dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentDto extends UserDto {
    private String gradeLevel;
    private String parentName;
    private String parentPhone;
}
