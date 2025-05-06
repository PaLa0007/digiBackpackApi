package com.digi_backpack_api.digiBackpackApi.Controllers;
import com.digi_backpack_api.digiBackpackApi.Dtos.RoleDto;
import com.digi_backpack_api.digiBackpackApi.Entities.Role;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> roles = Arrays.stream(Role.values())
                .map(role -> new RoleDto(role.name()))
                .toList();

        return ResponseEntity.ok(roles);
    }
}

