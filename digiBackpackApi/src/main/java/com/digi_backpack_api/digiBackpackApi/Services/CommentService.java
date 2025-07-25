package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.*;
import com.digi_backpack_api.digiBackpackApi.Entities.*;
import com.digi_backpack_api.digiBackpackApi.Repos.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ClassroomRepository classroomRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public CommentService(
            CommentRepository commentRepository,
            ClassroomRepository classroomRepository,
            AssignmentRepository assignmentRepository,
            UserRepository userRepository,
            StudentRepository studentRepository) {

        this.commentRepository = commentRepository;
        this.classroomRepository = classroomRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }

    public List<CommentDto> getCommentsForClassroom(Long classroomId) {
        return commentRepository.findByClassroomIdAndAssignmentIsNullAndLearningMaterialIsNull(classroomId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getCommentsForAssignment(Long assignmentId, Long userId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Comment> comments = commentRepository.findByAssignmentId(assignmentId).stream()
                .filter(c -> requester.getRole().equals(Role.TEACHER) ||
                        c.getCreatedBy().getId().equals(userId) ||
                        (c.getCreatedBy().getRole().equals(Role.TEACHER) &&
                                c.getRecipientStudent() != null &&
                                c.getRecipientStudent().getId().equals(userId)))
                .collect(Collectors.toList());

        return comments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CommentDto createClassroomComment(CommentDto dto, Long classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));
        User user = userRepository.findById(dto.getCreatedById())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setCreatedBy(user);
        comment.setClassroom(classroom);

        commentRepository.save(comment);
        return mapToDto(comment);
    }

    public CommentDto createAssignmentComment(CommentDto dto, Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        User user = userRepository.findById(dto.getCreatedById())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setCreatedBy(user);
        comment.setAssignment(assignment);
        comment.setClassroom(assignment.getClassroom()); // maintain link for future use

        // New: Set recipientStudent if provided and the commenter is a TEACHER
        if (dto.getRecipientStudentId() != null && user.getRole().equals(Role.TEACHER)) {
            Student recipientStudent = studentRepository.findById(dto.getRecipientStudentId())
                    .orElseThrow(() -> new RuntimeException("Recipient student not found"));
            comment.setRecipientStudent(recipientStudent);
        }

        commentRepository.save(comment);
        return mapToDto(comment);
    }

    public CommentDto updateComment(Long commentId, CommentDto dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getCreatedBy().getId().equals(dto.getCreatedById())) {
            throw new RuntimeException("You do not have permission to edit this comment.");
        }

        comment.setContent(dto.getContent());
        commentRepository.save(comment);
        return mapToDto(comment);
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("You do not have permission to delete this comment.");
        }

        commentRepository.delete(comment);
    }

    private CommentDto mapToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedById(comment.getCreatedBy().getId());
        dto.setCreatedByFirstName(comment.getCreatedBy().getFirstName());
        dto.setCreatedByLastName(comment.getCreatedBy().getLastName());
        dto.setCreatedByRole(comment.getCreatedBy().getRole().name());
        dto.setAssignmentId(comment.getAssignment() != null ? comment.getAssignment().getId() : null);
        dto.setLearningMaterialId(comment.getLearningMaterial() != null ? comment.getLearningMaterial().getId() : null);
        dto.setClassroomId(comment.getClassroom() != null ? comment.getClassroom().getId() : null);
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setRecipientStudentId(comment.getRecipientStudent() != null ? comment.getRecipientStudent().getId() : null);
        return dto;
    }
}
