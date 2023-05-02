package diploma.trackingApp.services;

import diploma.trackingApp.models.Role;
import diploma.trackingApp.models.Student;
import diploma.trackingApp.models.User;
import diploma.trackingApp.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void saveForStudent(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.ROLE_STUDENT));
        userRepository.save(user);
    }

    @Transactional
    public void saveForWorker(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.ROLE_WORKER));
        userRepository.save(user);
    }
    @Transactional
    public void delete(int id){
        userRepository.deleteById(id);
    }

    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public Optional<User> findByEmail(String email){
       return  userRepository.findByEmail(email);
    }

    public Optional<User> findByPassword(String password){
        return  userRepository.findByPassword(password);
    }

}
