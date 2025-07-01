package com.digi_backpack_api.digiBackpackApi.Services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.digi_backpack_api.digiBackpackApi.Dtos.LearningMaterialDto;
import com.digi_backpack_api.digiBackpackApi.Entities.Classroom;
import com.digi_backpack_api.digiBackpackApi.Entities.LearningMaterial;
import com.digi_backpack_api.digiBackpackApi.Entities.Teacher;
import com.digi_backpack_api.digiBackpackApi.Repos.LearningMaterialRepository;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LearningMaterialService {

    private final LearningMaterialRepository learningMaterialRepository;

    public LearningMaterialService(LearningMaterialRepository learningMaterialRepository) {
        this.learningMaterialRepository = learningMaterialRepository;
    }

    public List<LearningMaterialDto> getAllLearningMaterials() {
        return learningMaterialRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<LearningMaterialDto> getLearningMaterialsByClassroomId(Long classroomId) {
        return learningMaterialRepository.findByClassroomId(classroomId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public LearningMaterialDto uploadLearningMaterial(MultipartFile file, String title, String description,
            Long classroomId, Long uploadedById) throws IOException {
        String uploadDir = "uploads/";
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        LearningMaterial material = new LearningMaterial();
        material.setTitle(title);
        material.setDescription(description);
        material.setFileUrl("/" + uploadDir + fileName);
        material.setClassroom(new Classroom() {
            {
                setId(classroomId);
            }
        });
        material.setUploadedBy(new Teacher() {
            {
                setId(uploadedById);
            }
        });

        learningMaterialRepository.save(material);
        return mapToDto(material);
    }

    public void deleteLearningMaterial(Long id) {
        LearningMaterial material = learningMaterialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Learning material not found"));

        if (material.getFileUrl() != null) {
            String pathStr = material.getFileUrl().replaceFirst("^/", ""); // remove leading slash
            Path filePath = Paths.get(pathStr);

            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                System.err.println("Failed to delete file: " + filePath);
            }
        }

        learningMaterialRepository.delete(material);
    }

    public ResponseEntity<Resource> downloadLearningMaterial(String filename) throws IOException {
        Path filePath = Paths.get("uploads").resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    private LearningMaterialDto mapToDto(LearningMaterial material) {
        LearningMaterialDto dto = new LearningMaterialDto();
        dto.setId(material.getId());
        dto.setTitle(material.getTitle());
        dto.setDescription(material.getDescription());
        dto.setFileUrl(material.getFileUrl());
        dto.setUploadedById(material.getUploadedBy().getId());
        dto.setClassroomId(material.getClassroom().getId());
        return dto;
    }
}
