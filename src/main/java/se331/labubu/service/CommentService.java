package se331.labubu.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se331.labubu.dto.CommentDTO;
import se331.labubu.dto.CommentRequest;
import se331.labubu.entity.User;

public interface CommentService {
    Page<CommentDTO> getCommentsByNewsId(Long newsId, Pageable pageable, User currentUser);
    CommentDTO addComment(Long newsId, CommentRequest request, User currentUser);
    void softDeleteComment(Long commentId, Long newsId);
}