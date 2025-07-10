package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.StudentClassroomDto;
import com.digi_backpack_api.digiBackpackApi.Dtos.StudentDto;
import com.digi_backpack_api.digiBackpackApi.Entities.*;
import com.digi_backpack_api.digiBackpackApi.Repos.StudentClassroomRepository;
import com.digi_backpack_api.digiBackpackApi.Repos.StudentRepository;
import org.springframework.stereotype.Service;

import com.digi_backpack_api.digiBackpackApi.Dtos.StudentDashboardDto;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentClassroomRepository studentClassroomRepository;

    private final ClassroomService classroomService;
    private final AssignmentService assignmentService;
    private final LearningMaterialService learningMaterialService;

    public StudentService(StudentRepository studentRepository,
            StudentClassroomRepository studentClassroomRepository,
            ClassroomService classroomService,
            AssignmentService assignmentService,
            LearningMaterialService learningMaterialService) {
        this.studentRepository = studentRepository;
        this.studentClassroomRepository = studentClassroomRepository;
        this.classroomService = classroomService;
        this.assignmentService = assignmentService;
        this.learningMaterialService = learningMaterialService;
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

    public List<StudentDto> getStudentsByGrade(String gradeLevel) {
        return studentRepository.findByGradeLevel(gradeLevel).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<StudentClassroomDto> getByClassroom(Long classroomId) {
        return studentClassroomRepository.findByClassroomId(classroomId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
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

    public StudentDashboardDto getDashboardForStudent(Long studentId) {
        StudentDashboardDto dto = new StudentDashboardDto();

        dto.setClassroomCount(classroomService.getClassroomsByStudentId(studentId).size());

        dto.setUpcomingAssignments(
                (int) assignmentService.getAllAssignments().stream()
                        .filter(a -> a.getDueDate() != null && a.getDueDate().isAfter(LocalDate.now()))
                        .filter(a -> classroomService.getClassroomsByStudentId(studentId).stream()
                                .anyMatch(c -> c.getId().equals(a.getClassroomId())))
                        .count());

        dto.setMaterialsAvailable(
                (int) learningMaterialService.getAllLearningMaterials().stream()
                        .filter(m -> classroomService.getClassroomsByStudentId(studentId).stream()
                                .anyMatch(c -> c.getId().equals(m.getClassroomId())))
                        .count());

        return dto;
    }

    private void mapFromDto(Student student, StudentDto dto) {
        if (dto.getUsername() != null) {
            student.setUsername(dto.getUsername());
        }
        if (dto.getEmail() != null) {
            student.setEmail(dto.getEmail());
        }
        if (dto.getFirstName() != null) {
            student.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            student.setLastName(dto.getLastName());
        }
        if (dto.getGradeLevel() != null) {
            student.setGradeLevel(dto.getGradeLevel());
        }
        if (dto.getParentName() != null) {
            student.setParentName(dto.getParentName());
        }
        if (dto.getParentPhone() != null) {
            student.setParentPhone(dto.getParentPhone());
        }
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

    private StudentClassroomDto mapToDto(StudentClassroom sc) {
        StudentClassroomDto dto = new StudentClassroomDto();
        dto.setId(sc.getId());
        dto.setStudentId(sc.getStudent().getId());
        dto.setClassroomId(sc.getClassroom().getId());
        return dto;
    }

}
