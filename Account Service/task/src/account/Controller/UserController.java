package account.Controller;

import account.Model.Password;
import account.Model.User;
import account.Repo.RoleRepository;
import account.Repo.UserRepository;
import account.Services.EventService;
import account.Services.PasswordService;
import account.Services.PaymentService;
import account.Services.UserServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    PasswordService passwordService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    UserServices userServices;
    @Autowired
    EventService eventService;


    @PostMapping("/api/auth/signup")
    public User register(@Valid @RequestBody User user) {

        return userServices.createUser(user);
    }

    @PostMapping("/api/auth/changepass")
    public Map<String, String> updatePassword(@AuthenticationPrincipal UserDetails details, @Valid @RequestBody Password password) {

        User user = userRepository.findByEmailIgnoreCase(details.getUsername());

        passwordService.checkPassword(user, password);

        user.setPassword(encoder.encode(password.getNew_password()));

        userRepository.save(user);

        eventService.changePasswordEvent(user);
        log.info("Changed password for user " + user.getEmail());

        return Map.of("email", user.getEmail().toLowerCase(Locale.ROOT),
                "status", "The password has been updated successfully");
    }

    @GetMapping("/api/empl/payment")
    public Object getPayment(@AuthenticationPrincipal UserDetails userDetails,
                             @Valid @RequestParam(name = "period", required = false) String period) {
        if (period == null) {
            return paymentService.getAllPayments(userDetails.getUsername());
        } else {
            return paymentService.getPaymentByPeriod(userDetails.getUsername(), period);
        }

    }


}




