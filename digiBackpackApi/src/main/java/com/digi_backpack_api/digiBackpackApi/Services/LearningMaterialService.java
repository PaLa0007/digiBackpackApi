package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.LearningMaterialDto;
import com.digi_backpack_api.digiBackpackApi.Entities.LearningMaterial;
import com.digi_backpack_api.digiBackpackApi.Repos.LearningMaterialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
