package com.digi_backpack_api.digiBackpackApi.Repos;

import com.digi_backpack_api.digiBackpackApi.Entities.Student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByGradeLevel(String gradeLevel);
}
