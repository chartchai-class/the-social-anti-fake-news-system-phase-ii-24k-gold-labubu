package se331.labubu.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se331.labubu.entity.Participant;

import java.util.Optional;

public interface ParticipantDao {
    Page<Participant> getParticipants(Pageable pageRequest);
    Optional<Participant> findById(Long id);
    Participant save(Participant participant);
}