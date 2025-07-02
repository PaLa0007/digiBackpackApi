package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.*;
import com.digi_backpack_api.digiBackpackApi.Entities.*;
import com.digi_backpack_api.digiBackpackApi.Repos.*;
import org.springframework.stereotype.Service;

//import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final SchoolRepository schoolRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final AssignmentRepository assignmentRepository;
    private final LearningMaterialRepository learningMaterialRepository;
    private final CommentRepository commentRepository;

    public ClassroomService(
            ClassroomRepository classroomRepository,
            SchoolRepository schoolRepository,
            SubjectRepository subjectRepository,
            TeacherRepository teacherRepository,
            AssignmentRepository assignmentRepository,
            LearningMaterialRepository learningMaterialRepository,
            CommentRepository commentRepository) {
        this.classroomRepository = classroomRepository;
        this.schoolRepository = schoolRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.assignmentRepository = assignmentRepository;
        this.learningMaterialRepository = learningMaterialRepository;
        this.commentRepository = commentRepository;

    }

    public List<ClassroomDto> getAllClassrooms() {
        return classroomRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ClassroomDto> getClassroomsByTeacherId(Long teacherId) {
        return classroomRepository.findByTeacherId(teacherId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ClassroomDto getClassroomById(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found with ID: " + id));
        return mapToDto(classroom);
    }

    public ClassroomDto createClassroom(ClassroomDto dto) {
        Classroom classroom = new Classroom();
        classroom.setName(dto.getName());
        classroom.setGrade(dto.getGrade());
        if (dto.getSchoolId() != null) {
            classroom.setSchool(schoolRepository.findById(dto.getSchoolId()).orElse(null));
        }
        if (dto.getSubjectId() != null) {
            classroom.setSubject(subjectRepository.findById(dto.getSubjectId()).orElse(null));
        }
        if (dto.getTeacherId() != null) {
            classroom.setTeacher(teacherRepository.findById(dto.getTeacherId()).orElse(null));
        }
        return mapToDto(classroomRepository.save(classroom));
    }

    public ClassroomDto updateClassroom(Long id, ClassroomDto dto) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found with ID: " + id));

        classroom.setName(dto.getName());
        classroom.setGrade(dto.getGrade());
        if (dto.getSchoolId() != null) {
            classroom.setSchool(schoolRepository.findById(dto.getSchoolId()).orElse(null));
        }
        if (dto.getSubjectId() != null) {
            classroom.setSubject(subjectRepository.findById(dto.getSubjectId()).orElse(null));
        }
        if (dto.getTeacherId() != null) {
            classroom.setTeacher(teacherRepository.findById(dto.getTeacherId()).orElse(null));
        }
        return mapToDto(classroomRepository.save(classroom));
    }

    public void deleteClassroom(Long id) {
        if (!classroomRepository.existsById(id)) {
            throw new RuntimeException("Classroom not found with ID: " + id);
        }
        classroomRepository.deleteById(id);
    }

    public List<FeedItemDto> getClassroomFeed(Long classroomId) {
        List<FeedItemDto> feed = new ArrayList<>();

        List<Assignment> assignments = assignmentRepository.findByClassroomId(classroomId);
        for (Assignment a : assignments) {
            feed.add(new FeedItemDto(
                    a.getId(),
                    "assignment",
                    a.getTitle(),
                    a.getDescription(),
                    a.getCreatedBy().getFirstName() + " " + a.getCreatedBy().getLastName(),
                    a.getDueDate().atStartOfDay(),null));
        }

        List<LearningMaterial> materials = learningMaterialRepository.findByClassroomId(classroomId);
        for (LearningMaterial m : materials) {
            feed.add(new FeedItemDto(
                    m.getId(),
                    "material",
                    m.getTitle(),
                    m.getDescription(),
                    m.getUploadedBy().getFirstName() + " " + m.getUploadedBy().getLastName(),
                    m.getUploadedAt(), // Replace this with actual timestamp if you store it
                    m.getFileUrl()
            ));
        }

        List<Comment> messages = commentRepository
                .findByClassroomIdAndAssignmentIsNullAndLearningMaterialIsNull(classroomId);
        for (Comment c : messages) {
            feed.add(new FeedItemDto(
                    c.getId(),
                    "message",
                    null,
                    c.getContent(),
                    c.getCreatedBy().getFirstName() + " " + c.getCreatedBy().getLastName(),
                    c.getCreatedAt(), // Replace with actual timestamp if available
                    null
            ));
        }

        return feed.stream()
                .sorted(Comparator.comparing(FeedItemDto::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    private ClassroomDto mapToDto(Classroom classroom) {
        ClassroomDto dto = new ClassroomDto();
        dto.setId(classroom.getId());
        dto.setName(classroom.getName());
        dto.setGrade(classroom.getGrade());

        if (classroom.getSchool() != null) {
            dto.setSchoolId(classroom.getSchool().getId());
        }

        if (classroom.getSubject() != null) {
            dto.setSubjectId(classroom.getSubject().getId());
        }

        if (classroom.getTeacher() != null) {
            dto.setTeacherId(classroom.getTeacher().getId());
        }

        return dto;
    }
}
