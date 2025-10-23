package se331.labubu.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerDTO {
    Long id;
    String name;
    List<se331.labubu.dto.OrganizerOwnEventsDTO> ownEvents = new ArrayList<>();
    List<String> images;
}