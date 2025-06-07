package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.SubjectDto;
import com.digi_backpack_api.digiBackpackApi.Entities.School;
import com.digi_backpack_api.digiBackpackApi.Entities.Subject;
import com.digi_backpack_api.digiBackpackApi.Repos.SchoolRepository;
import com.digi_backpack_api.digiBackpackApi.Repos.SubjectRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SchoolRepository schoolRepository;

    public SubjectService(SubjectRepository subjectRepository, SchoolRepository schoolRepository) {
        this.subjectRepository = subjectRepository;
        this.schoolRepository = schoolRepository;
    }

    public List<SubjectDto> getSubjectsBySchool(Long schoolId) {
        return subjectRepository.findBySchoolId(schoolId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public SubjectDto createSubject(SubjectDto dto) {
        School school = schoolRepository.findById(dto.getSchoolId())
                .orElseThrow(() -> new EntityNotFoundException("School not found"));

        Subject subject = Subject.builder()
                .name(dto.getName())
                .school(school)
                .build();

        return toDto(subjectRepository.save(subject));
    }

    public SubjectDto updateSubject(Long id, SubjectDto dto) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));

        subject.setName(dto.getName());

        // Optional: allow changing school as well
        if (dto.getSchoolId() != null && !dto.getSchoolId().equals(subject.getSchool().getId())) {
            School school = schoolRepository.findById(dto.getSchoolId())
                    .orElseThrow(() -> new EntityNotFoundException("School not found"));
            subject.setSchool(school);
        }

        return toDto(subjectRepository.save(subject));
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }

    private SubjectDto toDto(Subject subject) {
        SubjectDto dto = new SubjectDto();
        dto.setId(subject.getId());
        dto.setName(subject.getName());
        dto.setSchoolId(subject.getSchool().getId());
        return dto;
    }
}
