package se331.labubu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotBlank(message = "Content cannot be empty")
    private String content;

    @NotBlank(message = "Vote is required")
    @Pattern(regexp = "fake|not-fake", message = "Vote must be either 'fake' or 'not-fake'")
    private String vote; // "fake" or "not-fake"

    private String imageUrl;
}