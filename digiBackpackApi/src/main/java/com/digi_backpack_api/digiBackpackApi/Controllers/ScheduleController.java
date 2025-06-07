package com.digi_backpack_api.digiBackpackApi.Controllers;

import com.digi_backpack_api.digiBackpackApi.Dtos.ScheduleDto;
import com.digi_backpack_api.digiBackpackApi.Services.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ScheduleDto create(@RequestBody ScheduleDto dto) {
        return scheduleService.createSchedule(dto);
    }

    @GetMapping
    public List<ScheduleDto> getAll() {
        return scheduleService.getAllSchedules();
    }

    @GetMapping("/{id}")
    public ScheduleDto getById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id);
    }

    @PutMapping("/{id}")
    public ScheduleDto update(@PathVariable Long id, @RequestBody ScheduleDto dto) {
        return scheduleService.updateSchedule(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
    }

    @GetMapping("/student/{studentId}")
    public List<ScheduleDto> getStudentSchedule(@PathVariable Long studentId) {
        return scheduleService.getScheduleForStudent(studentId);
    }
}
