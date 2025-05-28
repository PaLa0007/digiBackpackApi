package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.StudentSubmissionDto;
import com.digi_backpack_api.digiBackpackApi.Entities.Assignment;
import com.digi_backpack_api.digiBackpackApi.Entities.Student;
import com.digi_backpack_api.digiBackpackApi.Entities.StudentAssignmentSubmission;
import com.digi_backpack_api.digiBackpackApi.Repos.AssignmentRepository;
import com.digi_backpack_api.digiBackpackApi.Repos.StudentAssignmentSubmissionRepository;
import com.digi_backpack_api.digiBackpackApi.Repos.StudentRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentAssignmentSubmissionService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private final StudentAssignmentSubmissionRepository submissionRepo;
    private final StudentRepository studentRepo;
    private final AssignmentRepository assignmentRepo;

    public StudentAssignmentSubmissionService(
            StudentAssignmentSubmissionRepository submissionRepo,
            StudentRepository studentRepo,
            AssignmentRepository assignmentRepo) {
        this.submissionRepo = submissionRepo;
        this.studentRepo = studentRepo;
        this.assignmentRepo = assignmentRepo;
    }

    public void uploadSubmission(Long assignmentId, Long studentId, MultipartFile file) {
        try {
            Assignment assignment = assignmentRepo.findById(assignmentId)
                    .orElseThrow(() -> new RuntimeException("Assignment not found"));
            Student student = studentRepo.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir).resolve(filename);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            StudentAssignmentSubmission submission = new StudentAssignmentSubmission();
            submission.setFileName(filename);
            submission.setFilePath(path.toString());
            submission.setSubmittedAt(LocalDate.now());
            submission.setStudent(student);
            submission.setAssignment(assignment);

            submissionRepo.save(submission);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public List<StudentSubmissionDto> getSubmissionsForAssignment(Long assignmentId) {
        return submissionRepo.findByAssignmentId(assignmentId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deleteSubmission(Long id) {
        StudentAssignmentSubmission submission = submissionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        try {
            Files.deleteIfExists(Paths.get(submission.getFilePath()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }

        submissionRepo.deleteById(id);
    }

    public ResponseEntity<Resource> downloadSubmission(Long id) {
        StudentAssignmentSubmission submission = submissionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        Resource file = new FileSystemResource(submission.getFilePath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + submission.getFileName() + "\"")
                .body(file);
    }

    private StudentSubmissionDto mapToDto(StudentAssignmentSubmission sub) {
        StudentSubmissionDto dto = new StudentSubmissionDto();
        dto.setId(sub.getId());
        dto.setFileName(sub.getFileName());
        dto.setFilePath(sub.getFilePath());
        dto.setSubmittedAt(sub.getSubmittedAt());
        dto.setAssignmentId(sub.getAssignment().getId());
        dto.setStudentId(sub.getStudent().getId());
        return dto;
    }
}
