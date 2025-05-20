package com.digi_backpack_api.digiBackpackApi.Repos;

import com.digi_backpack_api.digiBackpackApi.Entities.LearningMaterial;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningMaterialRepository extends JpaRepository<LearningMaterial, Long> {
    long countByUploadedById(Long teacherId);

    List<LearningMaterial> findByClassroomId(Long classroomId);

}
