package se331.labubu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se331.labubu.dao.ParticipantDao;
import se331.labubu.entity.Participant;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    final ParticipantDao participantDao;

    @Override
    public List<Participant> getAllParticipants() {
        return participantDao.getParticipants(Pageable.unpaged()).getContent();
    }

    @Override
    public Page<Participant> getParticipants(Integer page, Integer pageSize) {
        return participantDao.getParticipants(PageRequest.of(page, pageSize));
    }

    @Override
    public Participant save(Participant participant) {
        return participantDao.save(participant);
    }
}