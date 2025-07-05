package com.digi_backpack_api.digiBackpackApi.Controllers;

import com.digi_backpack_api.digiBackpackApi.Services.StudentAssignmentSubmissionService;
import com.digi_backpack_api.digiBackpackApi.Dtos.StudentSubmissionDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class StudentAssignmentSubmissionController {

    private final StudentAssignmentSubmissionService submissionService;

    public StudentAssignmentSubmissionController(StudentAssignmentSubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping("/{assignmentId}/upload")
    public ResponseEntity<?> uploadSubmission(
            @PathVariable Long assignmentId,
            @RequestParam("studentId") Long studentId,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            @RequestParam(value = "description", required = false) String description) {

        submissionService.uploadSubmission(assignmentId, studentId, files, description);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<StudentSubmissionDto>> getSubmissionsByAssignment(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsForAssignment(assignmentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmission(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{submissionId}/download/{fileId}")
    public ResponseEntity<Resource> downloadSubmissionFile(
            @PathVariable Long submissionId,
            @PathVariable Long fileId) {
        return submissionService.downloadSubmissionFile(submissionId, fileId);
    }

}
