package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.StudentClassroomDto;
import com.digi_backpack_api.digiBackpackApi.Entities.StudentClassroom;
import com.digi_backpack_api.digiBackpackApi.Repos.StudentClassroomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentClassroomService {

    private final StudentClassroomRepository studentClassroomRepository;

    public StudentClassroomService(StudentClassroomRepository studentClassroomRepository) {
        this.studentClassroomRepository = studentClassroomRepository;
    }

    public List<StudentClassroomDto> getAllStudentClassrooms() {
        return studentClassroomRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private StudentClassroomDto mapToDto(StudentClassroom studentClassroom) {
        StudentClassroomDto dto = new StudentClassroomDto();
        dto.setId(studentClassroom.getId());
        dto.setStudentId(studentClassroom.getStudent().getId());
        dto.setClassroomId(studentClassroom.getClassroom().getId());
        return dto;
    }
}
