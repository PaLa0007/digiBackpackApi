package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.ScheduleDto;
import com.digi_backpack_api.digiBackpackApi.Entities.*;
import com.digi_backpack_api.digiBackpackApi.Repos.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ClassroomRepository classroomRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final StudentClassroomRepository studentClassroomRepository;

    public ScheduleService(
            ScheduleRepository scheduleRepository,
            ClassroomRepository classroomRepository,
            SubjectRepository subjectRepository,
            TeacherRepository teacherRepository,
            StudentClassroomRepository studentClassroomRepository) {
        this.scheduleRepository = scheduleRepository;
        this.classroomRepository = classroomRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.studentClassroomRepository = studentClassroomRepository;
    }

    public ScheduleDto createSchedule(ScheduleDto dto) {
        Schedule schedule = new Schedule();
        populateEntityFromDto(schedule, dto);
        schedule = scheduleRepository.save(schedule);
        return mapToDto(schedule);
    }

    public List<ScheduleDto> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ScheduleDto getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
        return mapToDto(schedule);
    }

    public List<ScheduleDto> getSchedulesByGrade(String grade) {
        List<Classroom> classrooms = classroomRepository.findByGrade(grade);
        List<Schedule> schedules = scheduleRepository.findByClassroomIn(classrooms);

        return schedules.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ScheduleDto updateSchedule(Long id, ScheduleDto dto) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
        populateEntityFromDto(schedule, dto);
        return mapToDto(scheduleRepository.save(schedule));
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    public List<ScheduleDto> getScheduleForStudent(Long studentId) {
        List<Classroom> classrooms = studentClassroomRepository.findClassroomsByStudentId(studentId);
        List<Schedule> schedules = scheduleRepository.findByClassroomIn(classrooms);
        return schedules.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<ScheduleDto> getScheduleForTeacher(Long teacherId) {
        List<Schedule> schedules = scheduleRepository.findByTeacherId(teacherId);
        return schedules.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private ScheduleDto mapToDto(Schedule schedule) {
        return ScheduleDto.builder()
                .id(schedule.getId())
                .dayOfWeek(schedule.getDayOfWeek().toString())
                .startTime(schedule.getStartTime().toString())
                .endTime(schedule.getEndTime().toString())
                .classroomId(schedule.getClassroom().getId())
                .classroomName(schedule.getClassroom().getName())
                .subjectId(schedule.getSubject().getId())
                .subjectName(schedule.getSubject().getName())
                .teacherId(schedule.getTeacher().getId())
                .teacherName(schedule.getTeacher().getFullName())
                .build();
    }

    private void populateEntityFromDto(Schedule schedule, ScheduleDto dto) {
        schedule.setDayOfWeek(DayOfWeek.valueOf(dto.getDayOfWeek().toUpperCase()));
        schedule.setStartTime(LocalTime.parse(dto.getStartTime()));
        schedule.setEndTime(LocalTime.parse(dto.getEndTime()));
        schedule.setClassroom(classroomRepository.findById(dto.getClassroomId())
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found")));
        schedule.setSubject(subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new EntityNotFoundException("Subject not found")));
        schedule.setTeacher(teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found")));
    }
}