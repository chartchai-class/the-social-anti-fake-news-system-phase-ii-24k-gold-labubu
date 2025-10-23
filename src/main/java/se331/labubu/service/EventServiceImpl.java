package se331.labubu.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import se331.labubu.dao.EventDao;
import se331.labubu.dao.OrganizerDao;
import se331.labubu.entity.Event;
import se331.labubu.entity.Organizer;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    @Autowired
    final EventDao eventDao;
    final OrganizerDao organizerDao;

    @Override
    public Integer getEventSize() {
        return eventDao.getEventSize();
    }

    @Override
    public Page<Event> getEvents(Integer pageSize, Integer page) {
        return eventDao.getEvents(pageSize, page);
    }

    @Override
    public Event getEvent(Long id) {
        return eventDao.getEvent(id);
    }

    @Override
    @Transactional
    public Event save(Event event) {
        Organizer organizer = organizerDao.findById(event.getOrganizer().getId())
                .orElseThrow(() -> new RuntimeException("Organizer not found with id: " + event.getOrganizer().getId()));

        event.setOrganizer(organizer);

        organizer.getOwnEvents().add(event);

        return eventDao.save(event);
    }

    @Override
    public Page<Event> getEvents(String title, Pageable pageable) {
        return eventDao.getEvents(title,pageable);
    }

    @Override
    public Page<Event> searchEvents(String keyword, PageRequest pageRequest) {
        return eventDao.searchEvents(keyword, pageRequest);
    }
}