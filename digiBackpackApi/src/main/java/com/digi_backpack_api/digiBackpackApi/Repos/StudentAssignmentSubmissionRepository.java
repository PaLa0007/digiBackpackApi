package com.digi_backpack_api.digiBackpackApi.Repos;

import com.digi_backpack_api.digiBackpackApi.Entities.StudentAssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface StudentAssignmentSubmissionRepository extends JpaRepository<StudentAssignmentSubmission, Long> {
    List<StudentAssignmentSubmission> findByAssignmentId(Long assignmentId);
}

