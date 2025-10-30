package se331.labubu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se331.labubu.dto.CommentDTO;
import se331.labubu.dto.CommentRequest;
import se331.labubu.entity.Comment;
import se331.labubu.entity.News;
import se331.labubu.entity.Role;
import se331.labubu.entity.User;
import se331.labubu.repository.CommentRepository;
import se331.labubu.repository.NewsRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;

    @Override
    public Page<CommentDTO> getCommentsByNewsId(Long newsId, Pageable pageable, User currentUser) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));

        Page<Comment> comments;

        // Admin sees all comments (including deleted)
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            comments = commentRepository.findByNewsId(newsId, pageable);
        } else {
            // Regular users only see non-deleted comments
            comments = commentRepository.findByNewsIdAndIsDeletedFalse(newsId, pageable);
        }

        return comments.map(this::convertToDTO);
    }

    @Override
    @Transactional
    public CommentDTO addComment(Long newsId, CommentRequest request, User currentUser) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));

        // Validate vote value
        if (!request.getVote().equals("fake") && !request.getVote().equals("not-fake")) {
            throw new RuntimeException("Invalid vote value. Must be 'fake' or 'not-fake'");
        }

        Comment comment = Comment.builder()
                .news(news)
                .user(currentUser)
                .content(request.getContent())
                .vote(request.getVote())
                .imageUrl(request.getImageUrl())
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        Comment savedComment = commentRepository.save(comment);

        // Update news status based on new vote
        news.updateStatus();
        newsRepository.save(news);

        return convertToDTO(savedComment);
    }

    @Override
    @Transactional
    public void softDeleteComment(Long commentId, Long newsId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getNews().getId().equals(newsId)) {
            throw new RuntimeException("Comment does not belong to this news");
        }

        comment.setIsDeleted(true);
        commentRepository.save(comment);

        // Update news status after deleting comment (vote is removed)
        News news = comment.getNews();
        news.updateStatus();
        newsRepository.save(news);
    }

    private CommentDTO convertToDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .newsId(comment.getNews().getId())
                .user(CommentDTO.UserDTO.builder()
                        .id(comment.getUser().getId())
                        .username(comment.getUser().getUsername())
                        .name(comment.getUser().getName())
                        .surname(comment.getUser().getSurname())
                        .profileImage(comment.getUser().getProfileImage())
                        .role(comment.getUser().getRole().name())
                        .build())
                .content(comment.getContent())
                .vote(comment.getVote())
                .imageUrl(comment.getImageUrl())
                .createdAt(comment.getCreatedAt())
                .isDeleted(comment.getIsDeleted())
                .build();
    }
}