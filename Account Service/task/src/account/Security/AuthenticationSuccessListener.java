package account.Security;

import account.Model.User;
import account.Repo.UserRepository;
import account.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserServices userServices;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String email = event.getAuthentication().getName();

        User user = userRepository.findByEmailIgnoreCase(email);

        userServices.resetFailedAttempts(user.getEmail());
    }
}
