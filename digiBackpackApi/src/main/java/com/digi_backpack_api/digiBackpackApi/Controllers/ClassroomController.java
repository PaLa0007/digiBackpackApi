package com.digi_backpack_api.digiBackpackApi.Controllers;

import com.digi_backpack_api.digiBackpackApi.Dtos.ClassroomDto;
import com.digi_backpack_api.digiBackpackApi.Dtos.FeedItemDto;
import com.digi_backpack_api.digiBackpackApi.Services.ClassroomService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {

    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @GetMapping
    public ResponseEntity<List<ClassroomDto>> getAllClassrooms() {
        return ResponseEntity.ok(classroomService.getAllClassrooms());
    }

    @GetMapping(params = "teacherId")
    public ResponseEntity<List<ClassroomDto>> getClassroomsByTeacher(@RequestParam Long teacherId) {
        return ResponseEntity.ok(classroomService.getClassroomsByTeacherId(teacherId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomDto> getClassroomById(@PathVariable Long id) {
        return ResponseEntity.ok(classroomService.getClassroomById(id));
    }

    @PostMapping
    public ResponseEntity<ClassroomDto> createClassroom(@RequestBody ClassroomDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(classroomService.createClassroom(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassroomDto> updateClassroom(@PathVariable Long id, @RequestBody ClassroomDto dto) {
        return ResponseEntity.ok(classroomService.updateClassroom(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long id) {
        classroomService.deleteClassroom(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/feed")
    public ResponseEntity<List<FeedItemDto>> getClassroomFeed(@PathVariable Long id) {
        return ResponseEntity.ok(classroomService.getClassroomFeed(id));
    }
}
