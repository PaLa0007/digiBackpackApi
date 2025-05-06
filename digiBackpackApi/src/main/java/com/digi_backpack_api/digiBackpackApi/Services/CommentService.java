package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.CommentDto;
import com.digi_backpack_api.digiBackpackApi.Entities.Comment;
import com.digi_backpack_api.digiBackpackApi.Repos.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<CommentDto> getAllComments() {
        return commentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CommentDto mapToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedById(comment.getCreatedBy().getId());
        dto.setAssignmentId(comment.getAssignment() != null ? comment.getAssignment().getId() : null);
        dto.setLearningMaterialId(comment.getLearningMaterial() != null ? comment.getLearningMaterial().getId() : null);
        return dto;
    }
}
