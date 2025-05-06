package com.digi_backpack_api.digiBackpackApi.Dtos;

import com.digi_backpack_api.digiBackpackApi.Entities.Role;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private Role role;
}
