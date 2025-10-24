package se331.labubu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewsRequest {

    @NotBlank(message = "Topic is required")
    @Size(min = 5, max = 200, message = "Topic must be between 5-200 characters")
    private String topic;

    @NotBlank(message = "Details are required")
    @Size(min = 10, message = "Details must be at least 10 characters")
    private String details;

    private String imageUrl; // Optional
}