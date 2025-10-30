package se331.labubu.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se331.labubu.dto.AuthenticationRequest;
import se331.labubu.dto.AuthenticationResponse;
import se331.labubu.dto.RegisterRequest;
import se331.labubu.dto.UserDTO;
import se331.labubu.entity.User;
import se331.labubu.service.AuthenticationService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authService;

  // Register - everyone becomes READER initially
  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
          @Valid @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(authService.register(request));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(
          @Valid @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(authService.authenticate(request));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
          @RequestBody AuthenticationRequest request
  ) {
    AuthenticationResponse result = authService.authenticate(request);
    return ResponseEntity.ok(result);
  }

  // Get current authenticated user
  @GetMapping("/me")
  public ResponseEntity<UserDTO> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(401).build();
    }

    User user = (User) authentication.getPrincipal();

    // Convert User entity to UserDTO
    UserDTO userDTO = UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .name(user.getName())
            .surname(user.getSurname())
            .role(user.getRole().name())
            .profileImage(user.getProfileImage())
            .build();

    return ResponseEntity.ok(userDTO);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request) {
    // Extract token from header
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String jwt = authHeader.substring(7);
      authService.logout(jwt);
    }
    return ResponseEntity.ok().build();
  }

//  @PostMapping("/refresh-token")
//  public void refreshToken(
//      HttpServletRequest request,
//      HttpServletResponse response
//  ) throws IOException {
//    authService.refreshToken(request, response);
//  }
}