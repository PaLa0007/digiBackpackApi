package com.digi_backpack_api.digiBackpackApi.Repos;

import com.digi_backpack_api.digiBackpackApi.Entities.Assignment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    long countByCreatedById(Long teacherId);

    List<Assignment> findByClassroomId(Long classroomId);

}
