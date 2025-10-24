package se331.labubu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se331.labubu.dto.AuthenticationRequest;
import se331.labubu.dto.AuthenticationResponse;
import se331.labubu.dto.RegisterRequest;
import se331.labubu.entity.Token;
import se331.labubu.repository.TokenRepository;
import se331.labubu.entity.TokenType;
import se331.labubu.entity.Role;
import se331.labubu.entity.User;
import se331.labubu.repository.UserRepository;
import se331.labubu.util.LabMapper;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    // Check if email already exists
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("Email already registered");
    }

    User user = User.builder()
            .name(request.getName())
            .surname(request.getSurname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.READER) // Initial role
            .profileImage(request.getProfileImage())
            .build();
    userRepository.save(user);

    var jwtToken = jwtService.generateToken(user);

    return AuthenticationResponse.builder()
            .token(jwtToken)
            .user(LabMapper.INSTANCE.getUserDTO(user))
            .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );

    var user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    var jwtToken = jwtService.generateToken(user);

    return AuthenticationResponse.builder()
            .token(jwtToken)
            .user(LabMapper.INSTANCE.getUserDTO(user))
            .build();
  }

//  private void saveUserToken(User user, String jwtToken) {
//    Token token = Token.builder()
//            .user(user)
//            .token(jwtToken)
//            .tokenType(TokenType.BEARER)
//            .expired(false)
//            .revoked(false)
//            .build();
//    tokenRepository.save(token);
//  }
//
//  private void revokeAllUserTokens(User user) {
//    List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
//    if (validUserTokens.isEmpty())
//      return;
//    validUserTokens.forEach(token -> {
//      token.setExpired(true);
//      token.setRevoked(true);
//    });
//    tokenRepository.saveAll(validUserTokens);
//  }
//
//  public void refreshToken(
//          HttpServletRequest request,
//          HttpServletResponse response
//  ) throws IOException {
//    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//    final String refreshToken;
//    final String userEmail;
//    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//      return;
//    }
//    refreshToken = authHeader.substring(7);
//    userEmail = jwtService.extractUsername(refreshToken);
//    if (userEmail != null) {
//      User user = this.repository.findByUsername(userEmail)
//              .orElseThrow();
//      if (jwtService.isTokenValid(refreshToken, user)) {
//        String accessToken = jwtService.generateToken(user);
//        revokeAllUserTokens(user);
//        saveUserToken(user, accessToken);
//        AuthenticationResponse authResponse = AuthenticationResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//      }
//    }
//  }
}
