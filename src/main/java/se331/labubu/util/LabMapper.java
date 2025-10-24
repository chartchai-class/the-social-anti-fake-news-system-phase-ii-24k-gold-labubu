package se331.labubu.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import se331.labubu.dto.*;
import se331.labubu.entity.*;

@Mapper
public interface LabMapper {
    LabMapper INSTANCE = Mappers.getMapper(LabMapper.class);

    // User mappings
    @Mapping(target = "password", ignore = true)
    UserDTO getUserDTO(User user);

    // News mappings
    @Mapping(target = "reporter", source = "reporter")
    NewsDTO getNewsDTO(News news);

    // Vote mappings
    @Mapping(target = "user", source = "user")
    VoteDTO getVoteDTO(Vote vote);

    // Comment mappings
    @Mapping(target = "user", source = "user")
    CommentDTO getCommentDTO(Comment comment);
}

