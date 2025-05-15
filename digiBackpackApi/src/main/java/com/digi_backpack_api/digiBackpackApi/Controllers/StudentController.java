package com.digi_backpack_api.digiBackpackApi.Controllers;

import com.digi_backpack_api.digiBackpackApi.Dtos.StudentDto;
import com.digi_backpack_api.digiBackpackApi.Services.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentDto>> searchStudents(@RequestParam String query) {
        return ResponseEntity.ok(studentService.searchStudents(query));
    }

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@RequestBody StudentDto dto) {
        return ResponseEntity.ok(studentService.createStudent(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @RequestBody StudentDto dto) {
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

}
