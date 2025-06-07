package com.digi_backpack_api.digiBackpackApi.Repos;

import com.digi_backpack_api.digiBackpackApi.Entities.Classroom;
import com.digi_backpack_api.digiBackpackApi.Entities.StudentClassroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface StudentClassroomRepository extends JpaRepository<StudentClassroom, Long> {
    List<StudentClassroom> findByClassroomId(Long classroomId);

    boolean existsByStudentIdAndClassroomId(Long studentId, Long classroomId);

    @Query("SELECT sc.classroom FROM StudentClassroom sc WHERE sc.student.id = :studentId")
    List<Classroom> findClassroomsByStudentId(Long studentId);
}
