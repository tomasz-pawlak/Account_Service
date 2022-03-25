package account.Services;

import account.Model.AccessModel;
import account.Model.User;
import account.Repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.Map;


@Service
@Slf4j
public class AdminService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    EventService eventService;

    public void updateValidator(User user, String role, String operation) {
        checkIfUserIsNull(user);
        checkInputRole(role);
        checkIfUserExists(user.getEmail());
        checkRoleType(user, role);
        checkIfUserIsAdmin(user);
        checkIfUserContainsAnyRole(user, role, operation);
        checkIfUserHaveOnlyOneRole(user, operation);
    }

    public void checkIfUserIsNull(User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
    }


    public void checkIfUserExists(String email) {
        if (!userRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
    }


    public void checkIfUserIsAdmin(User user) {
        if (user.getRoles().contains("ROLE_ADMINISTRATOR")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }
    }

    public void checkIfUserTryingToLockIsAdmin(User user) {
        if (user.getRoles().contains("ROLE_ADMINISTRATOR")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't lock the ADMINISTRATOR!");
        }
    }


    public void checkIfUserContainsAnyRole(User user, String role, String operation) {
        if (!user.getRoles().contains(("ROLE_" + role)) && operation.equals("REMOVE")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
        }
    }

    public void checkIfUserHaveOnlyOneRole(User user, String operation) {
        if (user.getRoles().size() < 2 && operation.equals("REMOVE")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
        }
    }

    public void checkRoleType(User user, String role) {
        if (user.getRoles().contains("ROLE_ADMINISTRATOR") && role.equals("ACCOUNTANT")) {
            throw new ResponseStatusException
                    (HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
        }
        if (user.getRoles().contains("ROLE_ADMINISTRATOR") && role.equals("USER")) {
            throw new ResponseStatusException
                    (HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
        }
        if (user.getRoles().contains("ROLE_ADMINISTRATOR") && role.equals("AUDITOR")) {
            throw new ResponseStatusException
                    (HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
        }
        if (user.getRoles().contains("ROLE_USER") && role.equals("ADMINISTRATOR")) {
            throw new ResponseStatusException
                    (HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
        }
        if (user.getRoles().contains("ROLE_ACCOUNTANT") && role.equals("ADMINISTRATOR")) {
            throw new ResponseStatusException
                    (HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
        }
    }

    public void checkInputRole(String role) {
        if (!role.matches("ADMINISTRATOR|AUDITOR|USER|ACCOUNTANT")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
        }
    }

    public Map<String, String> changeLockStatus(AccessModel accessModel) {

        User user = userRepository.findByEmailIgnoreCase(accessModel.getUser());

        checkIfUserTryingToLockIsAdmin(user);

        if (accessModel.getOperation().equals("LOCK")) {
            user.setAccountNonLocked(false);
            eventService.lockUserEvent(user);
            log.info("User " + user.getEmail() + " has been locked");
        } else if (accessModel.getOperation().equals("UNLOCK")) {
            user.setFailedAttempt(0);
            user.setAccountNonLocked(true);
            eventService.unlockUserEvent(user);
            log.info("User " + user.getEmail() + " has been unlocked");
        }

        userRepository.save(user);

        return Map.of("status", "User " + user.getEmail() + " " + accessModel.getOperation().toLowerCase(Locale.ROOT) + "ed!");
    }


}
