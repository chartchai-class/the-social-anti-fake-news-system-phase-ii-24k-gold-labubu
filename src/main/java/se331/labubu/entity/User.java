package se331.labubu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String surname;

  @Column(unique = true)
  private String email;
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;
  private String profileImage;

//  @ElementCollection
//  @Builder.Default
//  @LazyCollection(LazyCollectionOption.FALSE)
//  private List<Role> roles = new ArrayList<>();

//  @OneToMany(mappedBy = "user")
//  private List<Token> tokens;

//  @OneToMany(mappedBy = "reporter")
//  private List<News> newsReported;

  @OneToMany(mappedBy = "user")
  private List<Vote> votes;

  @OneToMany(mappedBy = "user")
  private List<Comment> comments;

  // UserDetails methods for Spring Security
//  @Override
//  public Collection<? extends GrantedAuthority> getAuthorities() {
//    return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
//  }

  // UserDetails methods for Spring Security
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
//    return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
    return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
