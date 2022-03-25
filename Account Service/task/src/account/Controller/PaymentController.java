package account.Controller;

import account.Model.Payment;
import account.Repo.PaymentRepository;
import account.Repo.UserRepository;
import account.Services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/acct/")
public class PaymentController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentService paymentService;

    @PostMapping("payments")
    public Map<String, String> createPayments(@Valid @RequestBody List<Payment> payments) {
        paymentService.saveAllPayments(payments);
        return Map.of("status", "Added successfully!");
    }

    @PutMapping("payments")
    public Map<String, String> createPayments(@Valid @RequestBody Payment payment) {
        paymentService.updatePayment(payment);
        return Map.of("status", "Updated successfully!");
    }


}
