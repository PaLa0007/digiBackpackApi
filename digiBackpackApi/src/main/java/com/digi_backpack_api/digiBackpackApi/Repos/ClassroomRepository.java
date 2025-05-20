package com.digi_backpack_api.digiBackpackApi.Repos;

import com.digi_backpack_api.digiBackpackApi.Entities.Classroom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    long countByTeacherId(Long teacherId);

    List<Classroom> findByTeacherId(Long teacherId);
}
