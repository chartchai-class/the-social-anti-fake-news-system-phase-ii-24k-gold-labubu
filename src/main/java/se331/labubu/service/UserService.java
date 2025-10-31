package se331.labubu.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se331.labubu.dto.UpdateProfileRequest;
import se331.labubu.dto.UserDTO;
import se331.labubu.entity.Role;
import se331.labubu.entity.User;

import java.util.List;

@Service
public interface UserService {
//    User save(User user);
//
//    @Transactional
//    User findByUsername(String username);
//    Page<UserDTO> getAllUsers(PageRequest of, Role role);
//    UserDTO getUserById(Long userId);
//    UserDTO getUserDTO(User currentUser);
//
//    //Admin
//    UserDTO upgradeToMember(Long userId);
//
//    UserDTO updateProfile(Integer id, UpdateProfileRequest request);

    UserDTO getUserDTO(User user);
    UserDTO getUserById(Long userId);
    Page<UserDTO> getAllUsers(Pageable pageable, Role role);

    // ==================== NEW ADMIN METHODS ====================

    /**
     * Get all users without pagination (Admin only)
     */
    List<UserDTO> getAllUsers();

    /**
     * Update user role (Admin only)
     * @param userId The user ID to update
     * @param role The new role as string (ADMIN, MEMBER, READER)
     * @return Updated user DTO
     */
    UserDTO updateUserRole(Long userId, String role);

    /**
     * Delete user (Admin only)
     * @param userId The user ID to delete
     */
    void deleteUser(Long userId);

    // ==================== EXISTING METHODS ====================

    UserDTO upgradeToMember(Long userId);
    UserDTO downgradeToReader(Long userId);
    UserDTO changeRole(Long userId, Role newRole);

    //Update user profile
    @Transactional
    UserDTO updateProfile(Long userId, UpdateProfileRequest request);
}