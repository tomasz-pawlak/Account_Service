package account.Services;

import account.Exceptions.NotSavePasswordException;
import account.Exceptions.UserExistException;
import account.Model.RoleEnum;
import account.Model.User;
import account.Repo.RoleRepository;
import account.Repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
public class UserServices {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordService passwordService;
    @Autowired
    EventService eventService;
    @Autowired
    PasswordEncoder encoder;

    public User createUser(User user) {
        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new UserExistException();
        }

        if (passwordService.hackedPassword.contains(user.getPassword())) {
            throw new NotSavePasswordException();
        }

        User newUser = new User(
                user.getId(),
                user.getName(),
                user.getLastname(),
                user.getEmail().toLowerCase(Locale.ROOT),
                user.getPassword()
        );

        if (userRepository.count() == 0) {
            newUser.addUserRole("ROLE_ADMINISTRATOR");
        } else {
            newUser.addUserRole("ROLE_USER");
        }

        newUser.setPassword(encoder.encode(newUser.getPassword()));

        userRepository.save(newUser);
        eventService.createUserEvent(user);
        log.info("Created user " + user.getEmail());

        return newUser;
    }

    public static final int MAX_FAILED_ATTEMPTS = 5;


    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        userRepository.updateFailedAttempts(newFailAttempts, user.getEmail());
    }

    public void resetFailedAttempts(String email) {
        userRepository.updateFailedAttempts(0, email);
    }

    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setFailedAttempt(5);
        userRepository.save(user);
    }

}
