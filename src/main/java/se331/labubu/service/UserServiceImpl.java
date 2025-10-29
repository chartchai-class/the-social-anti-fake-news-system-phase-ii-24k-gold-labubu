package se331.labubu.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se331.labubu.dto.UpdateProfileRequest;
import se331.labubu.dto.UserDTO;
import se331.labubu.entity.Role;
import se331.labubu.entity.User;
import se331.labubu.repository.UserRepository;
import se331.labubu.util.LabMapper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

//    @Override
//    @Transactional
//    public User save(User user) {
//        return userDao.save(user);
//    }

    @Override
    public UserDTO getUserDTO(User user) {
        return LabMapper.INSTANCE.getUserDTO(user);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return LabMapper.INSTANCE.getUserDTO(user);
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable, Role role) {
        Page<User> users;
        if (role != null) {
            users = userRepository.findByRole(role, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        return users.map(LabMapper.INSTANCE::getUserDTO);
    }

    //Upgrade reader to member
    @Override
    @Transactional
    public UserDTO upgradeToMember(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("Cannot modify admin role");
        }

        user.setRole(Role.MEMBER);
        userRepository.save(user);

        return LabMapper.INSTANCE.getUserDTO(user);
    }

    @Override
    public UserDTO downgradeToReader(Long userId) {
        return null;
    }

    @Override
    public UserDTO changeRole(Long userId, Role newRole) {
        return null;
    }

    //Update user profile
    @Override
    @Transactional
    public UserDTO updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getSurname() != null) {
            user.setSurname(request.getSurname());
        }
        if (request.getProfileImage() != null) {
            user.setProfileImage(request.getProfileImage());
        }

        userRepository.save(user);

        return LabMapper.INSTANCE.getUserDTO(user);
    }
}