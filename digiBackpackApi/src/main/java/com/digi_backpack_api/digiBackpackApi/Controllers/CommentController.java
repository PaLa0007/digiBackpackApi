package com.digi_backpack_api.digiBackpackApi.Controllers;

import com.digi_backpack_api.digiBackpackApi.Dtos.CommentDto;
import com.digi_backpack_api.digiBackpackApi.Services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<CommentDto>> getClassroomComments(@PathVariable Long classroomId) {
        return ResponseEntity.ok(commentService.getCommentsForClassroom(classroomId));
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<CommentDto>> getAssignmentComments(@PathVariable Long assignmentId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(commentService.getCommentsForAssignment(assignmentId, userId));
    }

    @PostMapping("/classroom/{classroomId}")
    public ResponseEntity<CommentDto> postClassroomComment(@PathVariable Long classroomId,
            @RequestBody CommentDto dto) {
        return ResponseEntity.ok(commentService.createClassroomComment(dto, classroomId));
    }

    @PostMapping("/assignment/{assignmentId}")
    public ResponseEntity<CommentDto> postAssignmentComment(@PathVariable Long assignmentId,
            @RequestBody CommentDto dto) {
        return ResponseEntity.ok(commentService.createAssignmentComment(dto, assignmentId));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long commentId, @RequestBody CommentDto dto) {
        return ResponseEntity.ok(commentService.updateComment(commentId, dto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @RequestParam Long userId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }

}
