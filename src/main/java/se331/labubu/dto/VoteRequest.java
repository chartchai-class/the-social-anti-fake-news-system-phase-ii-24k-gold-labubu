package se331.labubu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequest {

    @NotNull(message = "Vote decision is required")
    private Boolean isFake; // true = fake, false = real

    @NotBlank(message = "Comment is required")
    private String comment;

    private String imageUrl; // Optional
}