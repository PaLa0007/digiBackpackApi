package com.digi_backpack_api.digiBackpackApi.Controllers;

import com.digi_backpack_api.digiBackpackApi.Entities.User;
import com.digi_backpack_api.digiBackpackApi.Dtos.UserDto;
import com.digi_backpack_api.digiBackpackApi.Services.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username,
            @RequestParam String password,
            HttpSession session) {
        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("userId", user.getId());
            session.setAttribute("role", user.getRole().name());

            // Map User -> UserDto
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setEmail(user.getEmail());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setRole(user.getRole());

            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }

        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/by-school/{schoolId}")
    public ResponseEntity<List<UserDto>> getAdminsBySchool(@PathVariable Long schoolId) {
        return ResponseEntity.ok(userService.getAdminsBySchool(schoolId));
    }

    @PostMapping("/register-admin/{schoolId}")
    public ResponseEntity<UserDto> registerAdmin(
            @PathVariable Long schoolId,
            @RequestBody Map<String, Object> requestData) {
        String username = (String) requestData.get("username");
        String email = (String) requestData.get("email");
        String firstName = (String) requestData.get("firstName");
        String lastName = (String) requestData.get("lastName");
        String password = (String) requestData.get("password");

        UserDto createdAdmin = userService.registerSchoolAdmin(
                schoolId, username, email, firstName, lastName, password);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
    }

    @PutMapping("/{adminId}")
    public ResponseEntity<UserDto> updateAdmin(
            @PathVariable Long adminId,
            @RequestBody Map<String, Object> updates) {

        UserDto updatedAdmin = userService.updateSchoolAdmin(adminId, updates);
        return ResponseEntity.ok(updatedAdmin);
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long adminId) {
        userService.deleteSchoolAdmin(adminId);
        return ResponseEntity.ok("Admin deleted successfully");
    }
}
