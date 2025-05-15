package com.digi_backpack_api.digiBackpackApi.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentDto extends UserDto {
    private String gradeLevel;
    private String parentName;
    private String parentPhone;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
