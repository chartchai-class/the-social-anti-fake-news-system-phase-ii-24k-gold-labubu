package se331.labubu.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import se331.labubu.entity.User;
import se331.labubu.repository.UserRepository;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    final UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User save(User user) {
        return null;
    }

//    @Override
//    public User save(User user) {
//        return userRepository.save(user);
//    }
}