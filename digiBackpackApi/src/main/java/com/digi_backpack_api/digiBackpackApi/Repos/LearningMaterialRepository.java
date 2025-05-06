package com.digi_backpack_api.digiBackpackApi.Repos;

import com.digi_backpack_api.digiBackpackApi.Entities.LearningMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningMaterialRepository extends JpaRepository<LearningMaterial, Long> {
}
