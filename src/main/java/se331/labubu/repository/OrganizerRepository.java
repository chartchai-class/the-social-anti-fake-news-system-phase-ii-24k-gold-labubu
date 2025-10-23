package se331.labubu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se331.labubu.entity.Organizer;

public interface OrganizerRepository extends JpaRepository<Organizer, Long> {
}
