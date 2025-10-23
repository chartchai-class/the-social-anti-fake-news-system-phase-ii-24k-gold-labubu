package se331.labubu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se331.labubu.entity.Event;

import se331.labubu.service.EventService;
import se331.labubu.util.LabMapper;

@RestController
@RequiredArgsConstructor
public class EventController {
    final EventService eventService;

    @GetMapping("/events")
    public ResponseEntity<?> getEventLists(@RequestParam(value = "_limit",
    required = false) Integer perPage
    ,@RequestParam(value = "_page", required = false) Integer page,
                                           @RequestParam(value = "title", required = false) String title) {

        perPage = perPage == null ? 3 : perPage;
        page = page == null ? 1 : page;
        Page<Event> pageOutput;
        if (title == null) {
            pageOutput = eventService.getEvents(perPage, page);
        } else {
            pageOutput = eventService.getEvents(title, PageRequest.of(page-1, perPage));
        }
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.set("x-total-count", String.valueOf(pageOutput.getTotalElements()));
        return new ResponseEntity<>(
                LabMapper.INSTANCE.getEventDtoList(pageOutput.getContent()),
                responseHeader,
                HttpStatus.OK
        );
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<?> getEvent(@PathVariable("id") Long id) {
        Event output = eventService.getEvent(id);
        if (output != null) {
            return ResponseEntity.ok(LabMapper.INSTANCE.getEventDto(output));
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The given id is no found");
        }
    }

    @PostMapping("/events")
    public ResponseEntity<?> addEvent(@RequestBody Event event) {
        Event output = eventService.save(event);
        return ResponseEntity.ok(LabMapper.INSTANCE.getEventDto(output));
    }

    @GetMapping("/events/search")
    public ResponseEntity<?> searchEvents(
            @RequestParam(value = "_limit", required = false) Integer perPage,
            @RequestParam(value = "_page", required = false) Integer page,
            @RequestParam(value = "keyword") String keyword) {

        perPage = perPage == null ? 3 : perPage;
        page = page == null ? 1 : page;

        Page<Event> pageOutput = eventService.searchEvents(keyword, PageRequest.of(page-1, perPage));

        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("x-total-count", String.valueOf(pageOutput.getTotalElements()));

        return new ResponseEntity<>(
                LabMapper.INSTANCE.getEventDtoList(pageOutput.getContent()),
                responseHeader,
                HttpStatus.OK
        );
    }
}
