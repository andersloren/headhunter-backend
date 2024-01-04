package se.sprinta.headhunterbackend.user;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {
    // TODO: 04/01/2024 implements UserDetails... 

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public User save(User newUser) {
        return this.userRepository.save(newUser);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return null;
//        // TODO: 04/01/2024 fix later
//    }
}
