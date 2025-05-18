package com.digi_backpack_api.digiBackpackApi.Controllers;

import com.digi_backpack_api.digiBackpackApi.Dtos.CreateTeacherDto;
import com.digi_backpack_api.digiBackpackApi.Dtos.TeacherDashboardStatsDto;
import com.digi_backpack_api.digiBackpackApi.Dtos.TeacherDto;
import com.digi_backpack_api.digiBackpackApi.Services.TeacherService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeacherDto>> searchTeachers(@RequestParam String query) {
        return ResponseEntity.ok(teacherService.searchTeachers(query));
    }

    @GetMapping
    public ResponseEntity<List<TeacherDto>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getTeacherById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @PostMapping
    public ResponseEntity<TeacherDto> createTeacher(@RequestBody CreateTeacherDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.createTeacher(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherDto> updateTeacher(
            @PathVariable Long id,
            @RequestBody TeacherDto dto) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/dashboard-stats")
    public ResponseEntity<TeacherDashboardStatsDto> getDashboardStats(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getDashboardStats(id));
    }

}
