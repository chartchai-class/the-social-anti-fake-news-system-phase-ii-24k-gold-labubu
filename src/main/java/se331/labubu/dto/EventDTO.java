package se331.labubu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
//10
@Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class EventDTO {
        Long id;
        String category;
        String title;
        String description;
        String location;
        String date;
        String time;
        Boolean petAllowed;
        EventOrganizerDTO organizer;
        List<EventParticipantDTO> participants;
        List<String> images;
    }

