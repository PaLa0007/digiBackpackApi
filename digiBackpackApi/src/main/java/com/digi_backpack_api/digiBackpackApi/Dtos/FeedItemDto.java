package com.digi_backpack_api.digiBackpackApi.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FeedItemDto {
    private Long id;
    private String type;            // "assignment", "material", or "message"
    private String title;           // Optional (used for assignments/materials)
    private String description;     // Required (main text/content)
    private String createdBy;       // Full name of the teacher/user
    private LocalDateTime createdAt; // When it was posted

    // Convenience constructor for comments/messages without title
    public FeedItemDto(long id, String type, String description, String createdBy, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.title = null;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
} 
