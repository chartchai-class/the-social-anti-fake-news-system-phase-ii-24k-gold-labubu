package se331.labubu.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private UserDTO user;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
    private Boolean isDeleted;
}