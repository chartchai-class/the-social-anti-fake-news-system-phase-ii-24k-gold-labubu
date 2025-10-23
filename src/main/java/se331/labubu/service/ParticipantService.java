package se331.labubu.service;

import org.springframework.data.domain.Page;
import se331.labubu.entity.Participant;

import java.util.List;

public interface ParticipantService {
    List<Participant> getAllParticipants();
    Page<Participant> getParticipants(Integer page, Integer pageSize);
    Participant save(Participant participant);
}