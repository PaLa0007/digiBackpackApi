package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.ClassroomDto;
import com.digi_backpack_api.digiBackpackApi.Entities.Classroom;
import com.digi_backpack_api.digiBackpackApi.Repos.ClassroomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public List<ClassroomDto> getAllClassrooms() {
        return classroomRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ClassroomDto mapToDto(Classroom classroom) {
        ClassroomDto dto = new ClassroomDto();
        dto.setId(classroom.getId());
        dto.setName(classroom.getName());
        dto.setGrade(classroom.getGrade());
        dto.setSchoolId(classroom.getSchool().getId());
        dto.setSubjectId(classroom.getSubject().getId());
        dto.setTeacherId(classroom.getTeacher().getId());
        return dto;
    }
}
