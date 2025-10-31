package se331.labubu.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import se331.labubu.dto.*;
import se331.labubu.entity.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface LabMapper {
    LabMapper INSTANCE = Mappers.getMapper(LabMapper.class);

    // User mappings
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    UserDTO getUserDTO(User user);

    // News mappings - map comments and filter deleted ones
    @Mapping(target = "comments", expression = "java(mapComments(news.getComments()))")
    @Mapping(target = "fakeVoteCount", ignore = true)  // Set manually in service
    @Mapping(target = "realVoteCount", ignore = true)  // Set manually in service
    NewsDTO getNewsDTO(News news);

    // Comment mappings
    @Mapping(target = "newsId", source = "news.id")
    @Mapping(target = "user", source = "user")
    CommentDTO getCommentDTO(Comment comment);

    // Map User to CommentDTO.UserDTO
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    CommentDTO.UserDTO getCommentUserDTO(User user);

    // Vote mappings
    VoteDTO getVoteDTO(Vote vote);

    // Helper method to map and filter comments
    default List<CommentDTO> mapComments(List<Comment> comments) {
        if (comments == null) {
            return List.of();
        }
        return comments.stream()
                .filter(comment -> !comment.getIsDeleted())  // Filter out deleted comments
                .map(this::getCommentDTO)
                .collect(Collectors.toList());
    }
}