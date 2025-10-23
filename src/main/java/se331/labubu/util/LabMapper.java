package se331.labubu.util;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import se331.labubu.dto.ParticipantDTO;
import se331.labubu.entity.Event;
import se331.labubu.dto.EventDTO;
import se331.labubu.entity.Organizer;
import se331.labubu.entity.Participant;

import java.util.List;

@Mapper
public interface LabMapper {
        LabMapper INSTANCE = Mappers.getMapper(LabMapper.class);
        EventDTO getEventDto(Event event);
        List<EventDTO> getEventDto(List<Event> events);
        se331.labubu.dto.OrganizerDTO getOrganizerDTO(Organizer organizer);
        List<se331.labubu.dto.OrganizerDTO> getOrganizerDTO(List<Organizer> organizers);
    ParticipantDTO getParticipantDTO(Participant participant);
    List<ParticipantDTO> getParticipantDTO(List<Participant> participants);
    List<EventDTO> getEventDtoList(List<Event> events);
}

