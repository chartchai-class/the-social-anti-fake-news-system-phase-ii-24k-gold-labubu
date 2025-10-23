package se331.labubu.service;

import se331.labubu.entity.Organizer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrganizerService {
    Integer getOrganizerSize();
    List<Organizer> getAllOrganizer();
    Page<Organizer> getOrganizer(Integer page, Integer pageSize);
    Organizer getOrganizerId(Long id);
    Organizer save(Organizer organizer);
}