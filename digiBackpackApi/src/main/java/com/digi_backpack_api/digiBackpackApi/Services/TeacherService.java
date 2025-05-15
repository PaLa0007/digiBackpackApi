package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.TeacherDto;
import com.digi_backpack_api.digiBackpackApi.Dtos.CreateTeacherDto;
import com.digi_backpack_api.digiBackpackApi.Entities.School;
import com.digi_backpack_api.digiBackpackApi.Entities.Teacher;
import com.digi_backpack_api.digiBackpackApi.Entities.Role;
import com.digi_backpack_api.digiBackpackApi.Repos.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<TeacherDto> searchTeachers(String query) {
        return teacherRepository.findAll().stream()
                .filter(t -> (t.getFirstName() + " " + t.getLastName()).toLowerCase().contains(query.toLowerCase()) ||
                        t.getEmail().toLowerCase().contains(query.toLowerCase()) ||
                        t.getUsername().toLowerCase().contains(query.toLowerCase()))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<TeacherDto> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TeacherDto getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + id));
        return mapToDto(teacher);
    }

    public TeacherDto createTeacher(CreateTeacherDto dto) {
        Teacher teacher = new Teacher();
        teacher.setUsername(dto.getUsername());
        teacher.setEmail(dto.getEmail());
        teacher.setPassword(dto.getPassword()); // 🔐 hash if needed
        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setHireDate(dto.getHireDate());
        teacher.setSubjectSpecialization(dto.getSubjectSpecialization());
        teacher.setPhoneNumber(dto.getPhoneNumber());
        teacher.setRole(Role.TEACHER);

        if (dto.getSchoolId() != null) {
            School school = new School();
            school.setId(dto.getSchoolId());
            teacher.setSchool(school);
        }

        return mapToDto(teacherRepository.save(teacher));
    }

    public TeacherDto updateTeacher(Long id, TeacherDto dto) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + id));

        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setEmail(dto.getEmail());
        teacher.setHireDate(dto.getHireDate());
        teacher.setSubjectSpecialization(dto.getSubjectSpecialization());
        teacher.setPhoneNumber(dto.getPhoneNumber());

        return mapToDto(teacherRepository.save(teacher));
    }

    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new RuntimeException("Teacher not found with ID: " + id);
        }
        teacherRepository.deleteById(id);
    }

    private TeacherDto mapToDto(Teacher teacher) {
        TeacherDto dto = new TeacherDto();
        dto.setId(teacher.getId());
        dto.setUsername(teacher.getUsername());
        dto.setEmail(teacher.getEmail());
        dto.setFirstName(teacher.getFirstName());
        dto.setLastName(teacher.getLastName());
        dto.setRole(teacher.getRole());
        dto.setHireDate(teacher.getHireDate());
        dto.setSubjectSpecialization(teacher.getSubjectSpecialization());
        dto.setPhoneNumber(teacher.getPhoneNumber());
        return dto;
    }
}
