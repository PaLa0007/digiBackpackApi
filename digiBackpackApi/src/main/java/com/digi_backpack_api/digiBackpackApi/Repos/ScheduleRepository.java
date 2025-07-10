package com.digi_backpack_api.digiBackpackApi.Repos;

import com.digi_backpack_api.digiBackpackApi.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    //  Get all schedules for a specific classroom
    List<Schedule> findByClassroom(Classroom classroom);

    //  Get all schedules for multiple classrooms (used for students)
    List<Schedule> findByClassroomIn(List<Classroom> classrooms);

    //  Get schedules by classroom and day
    List<Schedule> findByClassroomAndDayOfWeek(Classroom classroom, DayOfWeek dayOfWeek);

    //  Get schedules by teacher
    List<Schedule> findByTeacherId(Long teacherId);

    //  Optional: find by subject or day
    List<Schedule> findBySubjectId(Long subjectId);
    List<Schedule> findByDayOfWeek(DayOfWeek dayOfWeek);
}
