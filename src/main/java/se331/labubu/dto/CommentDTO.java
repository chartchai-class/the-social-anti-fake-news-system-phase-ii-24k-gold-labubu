package se331.labubu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private Long newsId;
    private UserDTO user;
    private String content;
    private String vote; // "fake" or "not-fake"
    private String imageUrl;
    private LocalDateTime createdAt;
    private Boolean isDeleted;

    // Nested UserDTO class
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO {
        private Long id;
        private String username;
        private String name;
        private String surname;
        private String profileImage;
        private String role;
    }
}