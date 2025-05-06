package com.digi_backpack_api.digiBackpackApi.Controllers;

import com.digi_backpack_api.digiBackpackApi.Dtos.LearningMaterialDto;
import com.digi_backpack_api.digiBackpackApi.Services.LearningMaterialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
