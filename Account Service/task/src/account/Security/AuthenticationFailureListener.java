package account.Security;

import account.Exceptions.AdminLockException;
import account.Model.User;
import account.Repo.UserRepository;
import account.Services.AdminService;
import account.Services.EventService;
import account.Services.UserServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserServices userServices;
    @Autowired
    EventService eventService;
    @Autowired
    AdminService adminService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {

        String email = event.getAuthentication().getName();

        User user = userRepository.findByEmailIgnoreCase(email);

        if (user != null) {
            if (user.isAccountNonLocked()) {
                if (user.getFailedAttempt() < UserServices.MAX_FAILED_ATTEMPTS) {
                    userServices.increaseFailedAttempts(user);
                    eventService.failedLoginEvent(user.getEmail());
                    log.error("Failed to log");
                    if (userRepository.findByEmailIgnoreCase(email).getFailedAttempt() == 5) {
                        if (user.getRoles().contains("ROLE_ADMINISTRATOR")) {
                            throw new AdminLockException();
                        } else {
                            userServices.lock(user);
                            eventService.bruteForceEvent(user);
                            eventService.lockUserEvent(user);
                        }
                    }
                }
            }
        }
    }
}
