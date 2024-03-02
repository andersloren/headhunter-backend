package se.sprinta.headhunterbackend.user;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public User findByUserEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("user", email));
    }

    public User save(User newUser) {
        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        return this.userRepository.save(newUser);
    }

    public User update(String email, String roles) { // TODO: 31/01/2024 add username
        User foundUser = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("user", email));
        String rolesFixed = roles.replace("\"", "");
        foundUser.setRoles(rolesFixed);
        // TODO: 31/01/2024 add username
        return this.userRepository.save(foundUser);
    }


    public void delete(String email) {
        User foundUser = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("user", email));
        this.userRepository.delete(foundUser);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails = this.userRepository.findByEmail(email) // First, we need to find this user from database.
                .map(user -> new MyUserPrincipal(user)) // If found, wrap the returned user instance in a MyUserPrincipal instance.
                .orElseThrow(() -> new UsernameNotFoundException("email " + email + " is not found")); // Otherwise, throw an exception.
        return userDetails;
    }
}
