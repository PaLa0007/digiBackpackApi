package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.AssignmentDto;
import com.digi_backpack_api.digiBackpackApi.Entities.Assignment;
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
}
