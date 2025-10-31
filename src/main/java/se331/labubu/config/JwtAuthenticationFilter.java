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

    // Skip JWT validation for public endpoints
    String path = request.getServletPath();
    String method = request.getMethod();

// Only skip JWT for public auth endpoints and GET requests to /api/news
    if (path.equals("/api/v1/auth/register") ||
            path.equals("/api/v1/auth/login") ||
            path.equals("/api/v1/auth/authenticate") ||
            path.equals("/api/v1/auth/refresh-token") ||
            (path.startsWith("/api/news") && method.equals("GET"))) {  // <-- ONLY GET is public
      System.out.println("Skipping JWT validation for public endpoint: " + method + " " + path);
      filterChain.doFilter(request, response);
      return;
    }

    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      System.out.println("No auth header or doesn't start with Bearer for path: " + path);
      filterChain.doFilter(request, response);
      return;
    }
    jwt = authHeader.substring(7);
    System.out.println("JWT Token received for path: " + path);
    System.out.println("JWT Token: " + jwt.substring(0, Math.min(20, jwt.length())) + "...");

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
          System.out.println("Authentication successful for: " + userEmail);
        } else {
          System.out.println("Authentication FAILED - Token validation failed");
          System.out.println("JWT valid: " + jwtService.isTokenValid(jwt, userDetails));
          System.out.println("Token in DB valid: " + isTokenValid);
        }
      }
    } catch (Exception e) {
      System.out.println("Exception during JWT processing: " + e.getMessage());
      e.printStackTrace();
    }

    filterChain.doFilter(request, response);
  }
}