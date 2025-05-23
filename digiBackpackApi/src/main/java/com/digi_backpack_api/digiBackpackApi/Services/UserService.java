package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.UserDto;
import com.digi_backpack_api.digiBackpackApi.Entities.School;
import com.digi_backpack_api.digiBackpackApi.Entities.User;
import com.digi_backpack_api.digiBackpackApi.Entities.Role;
import com.digi_backpack_api.digiBackpackApi.Repos.SchoolRepository;
import com.digi_backpack_api.digiBackpackApi.Repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@Service
public class UserService {

    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, SchoolRepository schoolRepository) {
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
    }

    public User login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getAdminsBySchool(Long schoolId) {
        return userRepository.findBySchoolIdAndRole(schoolId, Role.SCHOOL_ADMIN)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UserDto registerSchoolAdmin(Long schoolId, String username, String email, String firstName, String lastName,
            String password) {
        // Find the school
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new RuntimeException("School not found"));

        // Create the admin user
        User admin = new User();
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setPassword(password); //
        admin.setRole(Role.SCHOOL_ADMIN);
        admin.setSchool(school); // 👈 link the school

        // Save
        User savedAdmin = userRepository.save(admin);

        return mapToDto(savedAdmin);
    }

    public UserDto updateSchoolAdmin(Long adminId, Map<String, Object> updates) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Update fields dynamically if they exist in the map
        if (updates.containsKey("username")) {
            admin.setUsername((String) updates.get("username"));
        }
        if (updates.containsKey("email")) {
            admin.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("firstName")) {
            admin.setFirstName((String) updates.get("firstName"));
        }
        if (updates.containsKey("lastName")) {
            admin.setLastName((String) updates.get("lastName"));
        }
        if (updates.containsKey("password")) {
            admin.setPassword((String) updates.get("password"));
        }

        User saved = userRepository.save(admin);
        return mapToDto(saved);
    }

    public void deleteSchoolAdmin(Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        userRepository.delete(admin);
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        dto.setSchoolId(user.getSchool() != null ? user.getSchool().getId() : null);
        return dto;
    }
}
