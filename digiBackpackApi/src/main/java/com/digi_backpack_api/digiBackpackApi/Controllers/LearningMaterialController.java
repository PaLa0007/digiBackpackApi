package com.digi_backpack_api.digiBackpackApi.Controllers;

import com.digi_backpack_api.digiBackpackApi.Dtos.LearningMaterialDto;
import com.digi_backpack_api.digiBackpackApi.Services.LearningMaterialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/learning-materials")
public class LearningMaterialController {

    private final LearningMaterialService learningMaterialService;

    public LearningMaterialController(LearningMaterialService learningMaterialService) {
        this.learningMaterialService = learningMaterialService;
    }

    @GetMapping
    public ResponseEntity<List<LearningMaterialDto>> getAllLearningMaterials() {
        return ResponseEntity.ok(learningMaterialService.getAllLearningMaterials());
    }

    @PostMapping("/upload")
    public ResponseEntity<LearningMaterialDto> uploadLearningMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("classroomId") Long classroomId,
            @RequestParam("uploadedById") Long uploadedById) throws IOException {

        return ResponseEntity.ok(
                learningMaterialService.uploadLearningMaterial(file, title, description, classroomId, uploadedById));
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
        return learningMaterialService.downloadLearningMaterial(filename);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLearningMaterial(@PathVariable Long id) {
        learningMaterialService.deleteLearningMaterial(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/classroom/{id}")
    public ResponseEntity<List<LearningMaterialDto>> getByClassroom(@PathVariable Long id) {
        return ResponseEntity.ok(learningMaterialService.getLearningMaterialsByClassroomId(id));
    }

}
