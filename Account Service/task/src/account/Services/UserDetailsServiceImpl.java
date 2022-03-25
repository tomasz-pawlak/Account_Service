package account.Services;

import account.Model.User;
import account.Repo.UserRepository;
import account.Security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    EventService eventService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmailIgnoreCase(email);

        if (user == null) {
            eventService.failedLoginEvent(email);
            throw new UsernameNotFoundException("User not found " + email);
        }

        return new UserDetailsImpl(user);
    }
}
