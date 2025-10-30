package se331.labubu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se331.labubu.entity.Comment;
import se331.labubu.entity.News;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Find comments by news (non-deleted)
    Page<Comment> findByNewsAndIsDeletedFalse(News news, Pageable pageable);

    // Find all comments by news (including deleted, for admin)
    Page<Comment> findByNews(News news, Pageable pageable);

    Page<Comment> findByNewsId(Long newsId, Pageable pageable);

    Page<Comment> findByNewsIdAndIsDeletedFalse(Long newsId, Pageable pageable);
}