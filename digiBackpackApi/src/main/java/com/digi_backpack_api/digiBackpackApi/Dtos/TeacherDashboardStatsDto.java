package com.digi_backpack_api.digiBackpackApi.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeacherDashboardStatsDto {
    private long classroomCount;
    private long assignmentCount;
    private long materialCount;
}
