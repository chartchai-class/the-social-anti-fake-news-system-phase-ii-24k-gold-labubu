// Submit votes (READER+)
package se331.labubu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se331.labubu.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se331.labubu.dto.VoteDTO;
import se331.labubu.dto.VoteRequest;
import se331.labubu.entity.User;
import se331.labubu.service.VoteService;

@RestController
@RequestMapping("/api/news/{newsId}/votes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    // GET votes for a news - EVERYONE can view
    @GetMapping
    public ResponseEntity<Page<VoteDTO>> getVotes(
            @PathVariable Long newsId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User currentUser
    ) {
        Page<VoteDTO> votes = voteService.getVotesByNewsId(
                newsId,
                PageRequest.of(page, size),
                currentUser
        );
        return ResponseEntity.ok(votes);
    }

    // POST vote - READER, MEMBER, ADMIN can vote
    @PostMapping
    @PreAuthorize("hasAnyRole('READER', 'MEMBER', 'ADMIN')")
    public ResponseEntity<VoteDTO> submitVote(
            @PathVariable Long newsId,
            @Valid @RequestBody VoteRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(
                voteService.submitVote(newsId, request, currentUser)
        );
    }
}
