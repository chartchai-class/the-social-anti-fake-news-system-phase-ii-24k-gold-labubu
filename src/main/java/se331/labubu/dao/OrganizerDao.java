package se331.labubu.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import se331.labubu.entity.Organizer;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface OrganizerDao {
    Integer getOrganizerSize();
    Page<Organizer> getOrganizer(Pageable pageRequest);
    Optional<Organizer> findById(Long id);
    Organizer getOrganizer(Long id);
    Organizer save(Organizer organizer);
    Organizer saveWithImages(Organizer organizer, List<MultipartFile> imageFiles) throws IOException;
}