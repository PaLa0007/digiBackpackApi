package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.StudentSubmissionDto;
import com.digi_backpack_api.digiBackpackApi.Entities.Assignment;
import com.digi_backpack_api.digiBackpackApi.Entities.Student;
import com.digi_backpack_api.digiBackpackApi.Entities.StudentAssignmentSubmission;
import com.digi_backpack_api.digiBackpackApi.Entities.SubmissionFile;
import com.digi_backpack_api.digiBackpackApi.Repos.AssignmentRepository;
import com.digi_backpack_api.digiBackpackApi.Repos.StudentAssignmentSubmissionRepository;
import com.digi_backpack_api.digiBackpackApi.Repos.StudentRepository;
import com.digi_backpack_api.digiBackpackApi.Dtos.StudentSubmissionFileDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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

    public void uploadSubmission(Long assignmentId, Long studentId, MultipartFile[] files, String description) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentAssignmentSubmission submission = new StudentAssignmentSubmission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setDescription(description);
        submission.setSubmittedAt(LocalDate.now());

        if (files != null) {
            for (MultipartFile file : files) {
                try {
                    String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path path = Paths.get(uploadDir).resolve(filename);
                    Files.createDirectories(path.getParent());
                    Files.write(path, file.getBytes());

                    SubmissionFile submissionFile = new SubmissionFile();
                    submissionFile.setFileName(filename);
                    submissionFile.setFilePath(path.toString());
                    submissionFile.setSubmission(submission);

                    submission.getFiles().add(submissionFile);

                } catch (IOException e) {
                    throw new RuntimeException("Failed to store file: " + file.getOriginalFilename(), e);
                }
            }
        }

        submissionRepo.save(submission);
    }

    public List<StudentSubmissionDto> getSubmissionsForAssignment(Long assignmentId) {
        return submissionRepo.findByAssignmentId(assignmentId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deleteSubmission(Long id) {
        StudentAssignmentSubmission submission = submissionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        for (SubmissionFile file : submission.getFiles()) {
            try {
                Files.deleteIfExists(Paths.get(file.getFilePath()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete file: " + file.getFileName(), e);
            }
        }

        submissionRepo.deleteById(id);
    }

    public ResponseEntity<Resource> downloadSubmissionFile(Long submissionId, Long fileId) {
        StudentAssignmentSubmission submission = submissionRepo.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        SubmissionFile file = submission.getFiles().stream()
                .filter(f -> f.getId().equals(fileId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("File not found in this submission"));

        Resource resource = new FileSystemResource(file.getFilePath());
        if (!resource.exists()) {
            throw new RuntimeException("File not found on server: " + file.getFileName());
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    private StudentSubmissionDto mapToDto(StudentAssignmentSubmission sub) {
        StudentSubmissionDto dto = new StudentSubmissionDto();
        dto.setId(sub.getId());
        dto.setSubmittedAt(sub.getSubmittedAt());
        dto.setAssignmentId(sub.getAssignment().getId());
        dto.setStudentId(sub.getStudent().getId());
        dto.setDescription(sub.getDescription());

        List<StudentSubmissionFileDto> fileDtos = sub.getFiles().stream()
                .map(f -> {
                    StudentSubmissionFileDto fileDto = new StudentSubmissionFileDto();
                    fileDto.setId(f.getId());
                    fileDto.setFileName(f.getFileName());
                    return fileDto;
                })
                .collect(Collectors.toList());
        dto.setFiles(fileDtos);

        dto.setStudentFirstName(sub.getStudent().getFirstName());
        dto.setStudentLastName(sub.getStudent().getLastName());

        return dto;
    }

}
