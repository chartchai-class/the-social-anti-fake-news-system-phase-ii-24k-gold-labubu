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
import se331.labubu.util.LabMapper;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;

    @Override
    public Page<CommentDTO> getCommentsByNewsId(Long newsId, Pageable pageable, User currentUser) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));

        // Admin can see deleted comments
        Page<Comment> comments;
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            comments = commentRepository.findByNews(news, pageable);
        } else {
            comments = commentRepository.findByNewsAndIsDeletedFalse(news, pageable);
        }

        return comments.map(LabMapper.INSTANCE::getCommentDTO);
    }

    @Override
    @Transactional
    public CommentDTO addComment(Long newsId, CommentRequest request, User currentUser) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));

        // Check if news is deleted
        if (news.getIsDeleted()) {
            throw new IllegalArgumentException("Cannot comment on deleted news");
        }

        Comment comment = Comment.builder()
                .news(news)
                .user(currentUser)
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .isDeleted(false)
                .build();

        commentRepository.save(comment);

        return LabMapper.INSTANCE.getCommentDTO(comment);
    }

    @Override
    @Transactional
    public void softDeleteComment(Long commentId, Long newsId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // Verify comment belongs to the news
        if (!comment.getNews().getId().equals(newsId)) {
            throw new IllegalArgumentException("Comment does not belong to this news");
        }

        comment.setIsDeleted(true);
        commentRepository.save(comment);

        // If this was a vote comment, recalculate news status
        // (This is already handled by Vote entity)
    }
}