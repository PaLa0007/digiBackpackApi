package com.digi_backpack_api.digiBackpackApi.Controllers;

import com.digi_backpack_api.digiBackpackApi.Dtos.SubjectDto;
import com.digi_backpack_api.digiBackpackApi.Services.SubjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/school/{schoolId}")
    public List<SubjectDto> getSubjectsBySchool(@PathVariable Long schoolId) {
        return subjectService.getSubjectsBySchool(schoolId);
    }

    @PostMapping
    public SubjectDto createSubject(@RequestBody SubjectDto dto) {
        return subjectService.createSubject(dto);
    }

    @PutMapping("/{id}")
    public SubjectDto updateSubject(@PathVariable Long id, @RequestBody SubjectDto dto) {
        return subjectService.updateSubject(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
    }
}
