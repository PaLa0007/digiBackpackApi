package com.digi_backpack_api.digiBackpackApi.Controllers;
import com.digi_backpack_api.digiBackpackApi.Dtos.CommentDto;
import com.digi_backpack_api.digiBackpackApi.Services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }
}
