package se331.labubu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {

    @NotBlank(message = "Comment content is required")
    private String content;

    private String imageUrl; // Optional
}