package com.digi_backpack_api.digiBackpackApi.Services;

import com.digi_backpack_api.digiBackpackApi.Dtos.SchoolDto;
import com.digi_backpack_api.digiBackpackApi.Entities.School;
import com.digi_backpack_api.digiBackpackApi.Repos.SchoolRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;

    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    public List<SchoolDto> getAllSchools() {
        return schoolRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public SchoolDto createSchool(SchoolDto schoolDto) {
        School school = new School();
        school.setName(schoolDto.getName());
        school.setAddress(schoolDto.getAddress());
        school.setCity(schoolDto.getCity());
        school.setCountry(schoolDto.getCountry());
    
        School savedSchool = schoolRepository.save(school);
        return mapToDto(savedSchool);
    }

    private SchoolDto mapToDto(School school) {
        SchoolDto dto = new SchoolDto();
        dto.setId(school.getId());
        dto.setName(school.getName());
        dto.setAddress(school.getAddress());
        dto.setCity(school.getCity());
        dto.setCountry(school.getCountry());
        return dto;
    }
}
