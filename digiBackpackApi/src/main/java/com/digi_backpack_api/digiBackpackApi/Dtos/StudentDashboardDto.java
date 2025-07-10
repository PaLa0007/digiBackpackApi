package com.digi_backpack_api.digiBackpackApi.Dtos;

import lombok.Data;

@Data
public class StudentDashboardDto {
    private long upcomingAssignments;
    private long materialsAvailable;
    private long classroomCount;
}
