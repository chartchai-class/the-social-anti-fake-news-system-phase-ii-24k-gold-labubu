package se331.labubu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import se331.labubu.service.ParticipantService;
import se331.labubu.util.LabMapper;

@RestController
@RequiredArgsConstructor
public class ParticipantController {

    final ParticipantService participantService;

    @GetMapping("/participants")
    ResponseEntity<?> getParticipants() {
        return ResponseEntity.ok(
                LabMapper.INSTANCE.getParticipantDTO(participantService.getAllParticipants())
        );
    }
}