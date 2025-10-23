package se331.labubu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se331.labubu.entity.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findAll();
    Page<Event> findByTitle(String title, Pageable pageRequest);
    Page<Event> findByTitleContaining(String title, Pageable pageRequest);
    Page<Event> findByTitleContainingOrDescriptionContaining(String title, String description, Pageable pageRequest);
    Page<Event> findByTitleContainingAndDescriptionContaining(String title, String description, Pageable pageRequest);

    // Search by title (case-insensitive)
    Page<Event> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Event> findByTitleContainingOrDescriptionContainingOrOrganizer_NameContaining(String keyword, String keyword1, String keyword2, Pageable page);
}