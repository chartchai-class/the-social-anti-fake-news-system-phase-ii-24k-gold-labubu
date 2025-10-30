package se331.labubu.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import se331.labubu.repository.TokenRepository;
import se331.labubu.service.JwtService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    if (request.getServletPath().contains("/api/v1/auth")) {
      filterChain.doFilter(request, response);
      return;
    }
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      System.out.println("No auth header or doesn't start with Bearer");
      filterChain.doFilter(request, response);
      return;
    }
    jwt = authHeader.substring(7);
    System.out.println("JWT Token: " + jwt);

    try {
      userEmail = jwtService.extractUsername(jwt);
      System.out.println("Extracted username: " + userEmail);

      if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        System.out.println("UserDetails loaded: " + userDetails.getUsername());

        boolean isTokenValid = tokenRepository.findByToken(jwt)
                .map(t -> {
                  System.out.println("Token found in DB - Expired: " + t.isExpired() + ", Revoked: " + t.isRevoked());
                  return !t.isExpired() && !t.isRevoked();
                })
                .orElseGet(() -> {
                  System.out.println("Token NOT found in database");
                  return false;
                });

        System.out.println("Is token valid in DB: " + isTokenValid);
        System.out.println("Is JWT valid: " + jwtService.isTokenValid(jwt, userDetails));

        if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                  userDetails,
                  null,
                  userDetails.getAuthorities()
          );
          authToken.setDetails(
                  new WebAuthenticationDetailsSource().buildDetails(request)
          );
          SecurityContextHolder.getContext().setAuthentication(authToken);
          System.out.println("Authentication successful!");
        } else {
          System.out.println("Authentication FAILED - Token validation failed");
        }
      }
    } catch (Exception e) {
      System.out.println("Exception during JWT processing: " + e.getMessage());
      e.printStackTrace();
    }

    filterChain.doFilter(request, response);
  }
}