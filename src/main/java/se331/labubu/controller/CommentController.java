// CRUD comments (role-based access)
package se331.labubu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se331.labubu.dto.CommentDTO;
import se331.labubu.entity.User;
import se331.labubu.service.CommentService;

@RestController
@RequestMapping("/api/news/{newsId}/comments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // GET comments - EVERYONE can view
    @GetMapping
    public ResponseEntity<Page<CommentDTO>> getComments(
            @PathVariable Long newsId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User currentUser
    ) {
        Page<CommentDTO> comments = commentService.getCommentsByNewsId(
                newsId,
                PageRequest.of(page, size),
                currentUser
        );
        return ResponseEntity.ok(comments);
    }

    // POST comment - READER, MEMBER, ADMIN can comment
    @PostMapping
    @PreAuthorize("hasAnyRole('READER', 'MEMBER', 'ADMIN')")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Long newsId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(
                commentService.addComment(newsId, request, currentUser)
        );
    }

    // Delete comment - Only ADMIN
    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long newsId,
            @PathVariable Long commentId
    ) {
        commentService.softDeleteComment(commentId, newsId);
        return ResponseEntity.noContent().build();
    }
}
