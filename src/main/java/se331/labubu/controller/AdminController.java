// Admin-only operations
package se331.labubu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se331.labubu.dto.UserDTO;
import se331.labubu.entity.Role;
import se331.labubu.service.UserService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')") // All methods require ADMIN
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    // GET all users
    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Role role // Filter by role
    ) {
        Page<UserDTO> users = userService.getAllUsers(
                PageRequest.of(page, size),
                role
        );
        return ResponseEntity.ok(users);
    }

    // PUT upgrade user to MEMBER
    @PutMapping("/users/{userId}/upgrade")
    public ResponseEntity<UserDTO> upgradeToMember(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.upgradeToMember(userId));
    }

//    // PUT downgrade user to READER
//    @PutMapping("/users/{userId}/downgrade")
//    public ResponseEntity<UserDTO> downgradeToReader(@PathVariable Long userId) {
//        return ResponseEntity.ok(userService.downgradeToReader(userId));
//    }
//
//    // PUT change user role
//    @PutMapping("/users/{userId}/role")
//    public ResponseEntity<UserDTO> changeUserRole(
//            @PathVariable Long userId,
//            @RequestParam Role newRole
//    ) {
//        return ResponseEntity.ok(userService.changeRole(userId, newRole));
//    }
}
