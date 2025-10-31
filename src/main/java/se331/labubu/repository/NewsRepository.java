package se331.labubu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se331.labubu.entity.News;
import se331.labubu.entity.NewsType;

public interface NewsRepository extends JpaRepository<News, Long> {

    // Find non-deleted news (for regular users)
    Page<News> findByIsDeletedFalse(Pageable pageable);

    // Find by status (non-deleted) for regular users
    Page<News> findByStatusAndIsDeletedFalse(NewsType status, Pageable pageable);

    // Find by status (including deleted) for admins
    Page<News> findByStatus(NewsType status, Pageable pageable);

    // Search by topic, details, or reporter name (non-deleted for regular users)
    @Query("SELECT n FROM News n JOIN n.reporter r WHERE " +
            "(LOWER(n.topic) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(n.details) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.surname) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND n.isDeleted = false")
    Page<News> searchNews(@Param("keyword") String keyword, Pageable pageable);

    // Search including deleted news (for admins)
    @Query("SELECT n FROM News n JOIN n.reporter r WHERE " +
            "(LOWER(n.topic) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(n.details) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.surname) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<News> searchNewsAll(@Param("keyword") String keyword, Pageable pageable);

    // Search by keyword and status (non-deleted for regular users)
    @Query("SELECT n FROM News n JOIN n.reporter r WHERE " +
            "(LOWER(n.topic) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(n.details) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.surname) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND n.status = :status AND n.isDeleted = false")
    Page<News> searchNewsByStatus(@Param("keyword") String keyword,
                                  @Param("status") NewsType status,
                                  Pageable pageable);

    // Search by keyword and status including deleted (for admins)
    @Query("SELECT n FROM News n JOIN n.reporter r WHERE " +
            "(LOWER(n.topic) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(n.details) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.surname) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND n.status = :status")
    Page<News> searchNewsByStatusAll(@Param("keyword") String keyword,
                                     @Param("status") NewsType status,
                                     Pageable pageable);

    // Admin can see all news including deleted
    Page<News> findAll(Pageable pageable);
}