package com.digi_backpack_api.digiBackpackApi.Repos;

import com.digi_backpack_api.digiBackpackApi.Entities.Comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByClassroomIdAndAssignmentIsNullAndLearningMaterialIsNull(Long classroomId);

}
