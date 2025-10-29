package se331.labubu.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import se331.labubu.dto.*;
import se331.labubu.entity.*;

@Mapper
public interface LabMapper {
    LabMapper INSTANCE = Mappers.getMapper(LabMapper.class);

    // User mappings - no @Mapping needed if password isn't in UserDTO
    UserDTO getUserDTO(User user);

    // News mappings
    NewsDTO getNewsDTO(News news);

    // Vote mappings
    VoteDTO getVoteDTO(Vote vote);

    // Comment mappings
    CommentDTO getCommentDTO(Comment comment);
}

