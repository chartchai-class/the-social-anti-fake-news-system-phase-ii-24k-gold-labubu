// CRUD news (role-based access)
package se331.labubu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se331.labubu.dto.NewsDTO;
import se331.labubu.entity.User;
import se331.labubu.service.NewsService;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    // Get all news - everyone can view
    @GetMapping
    public ResponseEntity<Page<NewsDTO>> getAllNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status, // "FAKE", "REAL", "PENDING"
            @RequestParam(required = false) String search,
            @AuthenticationPrincipal User currentUser // null if not logged in
    ) {
        Page<NewsDTO> news = newsService.getNews(
                PageRequest.of(page, size),
                status,
                search,
                currentUser // Pass to check if user is admin (to show deleted news)
        );
        return ResponseEntity.ok(news);
    }

    // Get single news - everyone can view
    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNewsById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(newsService.getNewsById(id, currentUser));
    }

    // Post new news - Only MEMBER
    @PostMapping
    @PreAuthorize("hasAnyRole('MEMBER', 'ADMIN')")
    public ResponseEntity<NewsDTO> createNews(
            @Valid @RequestBody NewsRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(newsService.createNews(request, currentUser));
    }

    // Delete news - Only ADMIN (soft delete)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.softDeleteNews(id);
        return ResponseEntity.noContent().build();
    }

    //News search
//    @GetMapping("/events/search")
//    public ResponseEntity<?> searchEvents(
//            @RequestParam(value = "_limit", required = false) Integer perPage,
//            @RequestParam(value = "_page", required = false) Integer page,
//            @RequestParam(value = "keyword") String keyword) {
//
//        perPage = perPage == null ? 3 : perPage;
//        page = page == null ? 1 : page;
//
//        Page<Event> pageOutput = eventService.searchEvents(keyword, PageRequest.of(page-1, perPage));
//
//        HttpHeaders responseHeader = new HttpHeaders();
//        responseHeader.set("x-total-count", String.valueOf(pageOutput.getTotalElements()));
//
//        return new ResponseEntity<>(
//                LabMapper.INSTANCE.getEventDtoList(pageOutput.getContent()),
//                responseHeader,
//                HttpStatus.OK
//        );
}
