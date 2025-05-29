package com.digi_backpack_api.digiBackpackApi.Controllers;

import com.digi_backpack_api.digiBackpackApi.Dtos.StudentClassroomDto;
import com.digi_backpack_api.digiBackpackApi.Services.StudentClassroomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-classrooms")
public class StudentClassroomController {

    private final StudentClassroomService studentClassroomService;

    public StudentClassroomController(StudentClassroomService studentClassroomService) {
        this.studentClassroomService = studentClassroomService;
    }

    @GetMapping
    public ResponseEntity<List<StudentClassroomDto>> getAllStudentClassrooms() {
        return ResponseEntity.ok(studentClassroomService.getAllStudentClassrooms());
    }

    @PostMapping
    public ResponseEntity<StudentClassroomDto> assignStudentToClassroom(@RequestBody StudentClassroomDto dto) {
        return ResponseEntity.ok(studentClassroomService.assignStudent(dto));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<StudentClassroomDto>> assignMultipleStudentsToClassroom(
            @RequestBody List<StudentClassroomDto> dtos) {
        return ResponseEntity.ok(studentClassroomService.assignStudentsBulk(dtos));
    }

}
