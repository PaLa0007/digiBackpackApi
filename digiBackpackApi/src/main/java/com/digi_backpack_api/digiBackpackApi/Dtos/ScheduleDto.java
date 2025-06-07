package com.digi_backpack_api.digiBackpackApi.Dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleDto {
    private Long id;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private Long classroomId;
    private String classroomName;
    private Long subjectId;
    private String subjectName;
    private Long teacherId;
    private String teacherName;
}
