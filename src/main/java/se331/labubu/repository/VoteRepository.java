package se331.labubu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se331.labubu.entity.News;
import se331.labubu.entity.User;
import se331.labubu.entity.Vote;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    // Find votes by news (non-deleted)
    Page<Vote> findByNewsAndIsDeletedFalse(News news, Pageable pageable);

    Page<Vote> findByNews(News news, Pageable pageable);

    // Check if user already voted on this news
    Optional<Vote> findByNewsAndUser(News news, User user);

    // Count votes for news
    long countByNewsAndIsFakeAndIsDeletedFalse(News news, Boolean isFake);
}