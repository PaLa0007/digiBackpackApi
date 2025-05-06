package com.digi_backpack_api.digiBackpackApi.Controllers;

import com.digi_backpack_api.digiBackpackApi.Dtos.AssignmentDto;
import com.digi_backpack_api.digiBackpackApi.Services.AssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public ResponseEntity<List<AssignmentDto>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }
}
