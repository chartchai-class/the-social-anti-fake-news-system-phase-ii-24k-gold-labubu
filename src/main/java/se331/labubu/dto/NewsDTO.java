package se331.labubu.dto;

import lombok.Data;
import se331.labubu.entity.NewsType;
import java.time.LocalDateTime;

@Data
public class NewsDTO {
    private Long id;
    private String topic;
    private String details;
    private String imageUrl;
    private NewsType status;
    private UserDTO reporter;
    private LocalDateTime createdAt;
    private Boolean isDeleted;
    private long fakeVoteCount;
    private long realVoteCount;
}