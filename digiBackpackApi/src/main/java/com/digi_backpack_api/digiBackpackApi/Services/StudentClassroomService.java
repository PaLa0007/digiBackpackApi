package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.StudentClassroomDto;
import com.digi_backpack_api.digiBackpackApi.Entities.StudentClassroom;
import com.digi_backpack_api.digiBackpackApi.Repos.ClassroomRepository;
import com.digi_backpack_api.digiBackpackApi.Repos.StudentClassroomRepository;
import com.digi_backpack_api.digiBackpackApi.Repos.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentClassroomService {

    private final StudentClassroomRepository studentClassroomRepository;
    private final StudentRepository studentRepository;
    private final ClassroomRepository classroomRepository;

    public StudentClassroomService(
            StudentClassroomRepository studentClassroomRepository,
            StudentRepository studentRepository,
            ClassroomRepository classroomRepository) {
        this.studentClassroomRepository = studentClassroomRepository;
        this.studentRepository = studentRepository;
        this.classroomRepository = classroomRepository;
    }

    public List<StudentClassroomDto> getAllStudentClassrooms() {
        return studentClassroomRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public StudentClassroomDto assignStudent(StudentClassroomDto dto) {
        boolean exists = studentClassroomRepository.existsByStudentIdAndClassroomId(
                dto.getStudentId(), dto.getClassroomId());

        if (exists) {
            throw new IllegalArgumentException("Student already assigned to this classroom");
        }

        StudentClassroom sc = new StudentClassroom();
        sc.setStudent(studentRepository.findById(dto.getStudentId()).orElseThrow());
        sc.setClassroom(classroomRepository.findById(dto.getClassroomId()).orElseThrow());

        return mapToDto(studentClassroomRepository.save(sc));
    }

    public List<StudentClassroomDto> assignStudentsBulk(List<StudentClassroomDto> dtos) {
        List<StudentClassroom> entities = dtos.stream()
                .filter(dto -> !studentClassroomRepository.existsByStudentIdAndClassroomId(dto.getStudentId(),
                        dto.getClassroomId()))
                .map(dto -> {
                    StudentClassroom sc = new StudentClassroom();
                    sc.setStudent(studentRepository.findById(dto.getStudentId()).orElseThrow());
                    sc.setClassroom(classroomRepository.findById(dto.getClassroomId()).orElseThrow());
                    return sc;
                }).collect(Collectors.toList());

        return studentClassroomRepository.saveAll(entities).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public boolean removeStudent(Long id) {
        if (!studentClassroomRepository.existsById(id)) {
            return false;
        }
        studentClassroomRepository.deleteById(id);
        return true;
    }

    private StudentClassroomDto mapToDto(StudentClassroom studentClassroom) {
        StudentClassroomDto dto = new StudentClassroomDto();
        dto.setId(studentClassroom.getId());
        dto.setStudentId(studentClassroom.getStudent().getId());
        dto.setClassroomId(studentClassroom.getClassroom().getId());
        return dto;
    }
}
