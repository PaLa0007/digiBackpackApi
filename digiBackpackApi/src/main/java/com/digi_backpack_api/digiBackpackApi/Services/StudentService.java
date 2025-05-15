package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.StudentDto;
import com.digi_backpack_api.digiBackpackApi.Entities.School;
import com.digi_backpack_api.digiBackpackApi.Entities.Student;
import com.digi_backpack_api.digiBackpackApi.Entities.Role;
import com.digi_backpack_api.digiBackpackApi.Repos.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
        return mapToDto(student);
    }

    public List<StudentDto> searchStudents(String query) {
        return studentRepository.findAll().stream()
                .filter(s -> (s.getFirstName() + " " + s.getLastName()).toLowerCase().contains(query.toLowerCase())
                        || s.getEmail().toLowerCase().contains(query.toLowerCase())
                        || s.getUsername().toLowerCase().contains(query.toLowerCase()))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public StudentDto createStudent(StudentDto dto) {
        Student student = new Student();
        mapFromDto(student, dto);
        student.setPassword(dto.getPassword()); // Only on create
        student.setRole(Role.STUDENT);
        return mapToDto(studentRepository.save(student));
    }

    public StudentDto updateStudent(Long id, StudentDto dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
        mapFromDto(student, dto);
        return mapToDto(studentRepository.save(student));
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    private void mapFromDto(Student student, StudentDto dto) {
        student.setUsername(dto.getUsername());
        student.setEmail(dto.getEmail());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setGradeLevel(dto.getGradeLevel());
        student.setParentName(dto.getParentName());
        student.setParentPhone(dto.getParentPhone());

        if (dto.getSchoolId() != null) {
            School school = new School();
            school.setId(dto.getSchoolId());
            student.setSchool(school);
        }
    }

    private StudentDto mapToDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setUsername(student.getUsername());
        dto.setEmail(student.getEmail());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setRole(student.getRole());
        dto.setGradeLevel(student.getGradeLevel());
        dto.setParentName(student.getParentName());
        dto.setParentPhone(student.getParentPhone());
        return dto;
    }
}
