package se331.labubu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se331.labubu.entity.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}