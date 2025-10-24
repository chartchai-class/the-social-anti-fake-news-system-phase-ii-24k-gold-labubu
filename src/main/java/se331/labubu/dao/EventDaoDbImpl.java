package se331.labubu.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import se331.labubu.repository.EventRepository;

@Repository
@RequiredArgsConstructor
@Profile("db")
public class EventDaoDbImpl implements EventDao {
    final EventRepository eventRepository;
    @Override
    public Integer getEventSize() {
        return Math.toIntExact(eventRepository.count());
    }

    @Override
    public Page<Event> getEvents(Integer pageSize, Integer page) {
        // Handle null parameters with defaults
        int size = pageSize != null ? pageSize : 3;
        int pageNum = page != null ? page : 1;

        return eventRepository.findAll(PageRequest.of(pageNum - 1, size));
    }

    @Override
    public Event getEvent(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

//    @Override
//    public Page<Event> getEvents(String title, Pageable page) {
//        return eventRepository.findByTitleContainingOrDescriptionContainingOrOrganizer_NameContaining(title, title, title, page);
//    }

    @Override
    public Page<Event> getEvents(String title, Pageable page) {
        return eventRepository.findByTitleContainingOrDescriptionContaining(title,title, page);
    }

    @Override
    public Page<Event> searchEvents(String keyword, Pageable page) {
        return eventRepository.findByTitleContainingOrDescriptionContainingOrOrganizer_NameContaining(keyword, keyword, keyword, page);
    }

}
