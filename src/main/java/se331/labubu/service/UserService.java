package se331.labubu.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import se331.labubu.dto.UserDTO;
import se331.labubu.entity.Role;
import se331.labubu.entity.User;

@Service
@RequiredArgsConstructor
public interface UserService {
    User save(User user);

    @Transactional
    User findByUsername(String username);
    Page<UserDTO> getAllUsers(PageRequest of, Role role);
    UserDTO getUserById(Long userId);
    UserDTO getUserDTO(User currentUser);

    //Admin
    UserDTO upgradeToMember(Long userId);

    UserDTO updateProfile(Integer id, UpdateProfileRequest request);
}