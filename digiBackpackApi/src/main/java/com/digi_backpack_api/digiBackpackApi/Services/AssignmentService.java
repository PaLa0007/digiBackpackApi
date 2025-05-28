package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.AssignmentDto;
import com.digi_backpack_api.digiBackpackApi.Entities.Assignment;
import com.digi_backpack_api.digiBackpackApi.Entities.Classroom;
import com.digi_backpack_api.digiBackpackApi.Entities.Teacher;
import com.digi_backpack_api.digiBackpackApi.Repos.AssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    public List<AssignmentDto> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private AssignmentDto mapToDto(Assignment assignment) {
        AssignmentDto dto = new AssignmentDto();
        dto.setId(assignment.getId());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setDueDate(assignment.getDueDate());
        dto.setCreatedById(assignment.getCreatedBy().getId());
        dto.setClassroomId(assignment.getClassroom().getId());
        return dto;
    }

    public AssignmentDto createAssignment(AssignmentDto dto) {
        Assignment assignment = new Assignment();
        assignment.setTitle(dto.getTitle());
        assignment.setDescription(dto.getDescription());
        assignment.setDueDate(dto.getDueDate());
        assignment.setCreatedBy(new Teacher());
        assignment.getCreatedBy().setId(dto.getCreatedById());
        assignment.setClassroom(new Classroom());
        assignment.getClassroom().setId(dto.getClassroomId());
        return mapToDto(assignmentRepository.save(assignment));
    }

    public AssignmentDto updateAssignment(Long id, AssignmentDto dto) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignment.setTitle(dto.getTitle());
        assignment.setDescription(dto.getDescription());
        assignment.setDueDate(dto.getDueDate());
        return mapToDto(assignmentRepository.save(assignment));
    }

    public void deleteAssignment(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new RuntimeException("Assignment not found");
        }
        assignmentRepository.deleteById(id);
    }

    public AssignmentDto getAssignmentById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        return mapToDto(assignment);
    }

}
