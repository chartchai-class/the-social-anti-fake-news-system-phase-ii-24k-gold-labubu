package se331.labubu.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se331.labubu.entity.Event;

public interface EventDao {
    Integer getEventSize();
    Page<Event> getEvents(Integer pageSize, Integer page);
    Event getEvent(Long id);
    Event save(Event event);
    Page<Event> getEvents(String name, Pageable page);

    Page<Event> searchEvents(String keyword, Pageable page);
}
