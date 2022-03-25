package account.Controller;

import account.Model.AccessModel;
import account.Model.Role;
import account.Model.User;
import account.Repo.UserRepository;
import account.Services.AdminService;
import account.Services.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("api/admin/")
public class AdminController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AdminService adminService;
    @Autowired
    EventService eventService;


    @PutMapping("user/role")
    public User changeUserRole(@Valid @RequestBody Role role) {

        User user = userRepository.findByEmailIgnoreCase(role.getUser());

        adminService.updateValidator(user, role.getRole(), role.getOperation());

        if (role.getOperation().equals("GRANT")) {
            user.addUserRole("ROLE_" + role.getRole());
            userRepository.save(user);
            eventService.grantRoleEvent(user, role.getRole());
        }

        if (role.getOperation().equals("REMOVE")) {
            user.removeUserRole("ROLE_" + role.getRole());
            userRepository.save(user);
            eventService.removeRoleEvent(user, role.getRole());
        }

        return user;
    }

    @GetMapping("user")
    public List<User> showAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @DeleteMapping(value = {"user", "user/{email}"})
    public Map<String, String> deleteUser(@PathVariable String email) {

        adminService.checkIfUserExists(email);

        User user = userRepository.findByEmailIgnoreCase(email);

        adminService.checkIfUserIsAdmin(user);

        userRepository.delete(user);
        eventService.deleteUserEvent(user);
        log.info("Deleted user " + user.getEmail());

        return Map.of("user", email, "status", "Deleted successfully!");
    }

    @PutMapping("user/access")
    public Map<String, String> manageUserAccess(@RequestBody AccessModel accessModel) {
        return adminService.changeLockStatus(accessModel);
    }
}
