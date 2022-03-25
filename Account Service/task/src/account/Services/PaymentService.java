package account.Services;

import account.Model.Payment;
import account.Model.PaymentResponse;
import account.Model.User;
import account.Repo.PaymentRepository;
import account.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    UserRepository userRepository;

    public void saveAllPayments(List<Payment> payments) {

        for (Payment payment : payments
        ) {
            if (paymentRepository.existsByEmployeeAndPeriod(payment.getEmployee(), payment.getPeriod())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The period of " + payment.getPeriod() + " is already exist for " + payment.getEmployee());
            } else {
                savePayment(payment);
            }
        }
    }

    public void savePayment(Payment payment) {
        payment.setUser(userRepository.findByEmailIgnoreCase(payment.getEmployee()));
        if (payment.getSalary() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Salary must be non negative!");
        } else if (!payment.getPeriod().matches("0[1-9]-\\d{4}|(1[012])-\\d{4}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "payments[1].period: Wrong date!");

        } else {
            paymentRepository.save(payment);
        }
    }

    public void updatePayment(Payment payment) {
        payment.setUser(userRepository.findByEmailIgnoreCase(payment.getEmployee()));
        paymentRepository.update(payment.getEmployee(), payment.getPeriod(), payment.getSalary());
    }

    public PaymentResponse getPaymentByPeriod(String email, String period) {
        if (!period.matches("0[1-9]-\\d{4}|(1[012])-\\d{4}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "payments[1].period: Wrong date!");
        }

        User user = userRepository.findByEmailIgnoreCase(email);

        Payment payment = paymentRepository.findPaymentByEmployeeAndPeriod(email.toLowerCase(Locale.ROOT), period);

        return new PaymentResponse(
                user.getName(),
                user.getLastname(),
                payment.getPeriod(),
                payment.getSalary()
        );
    }

    public List<PaymentResponse> getAllPayments(String email) {

        User user = userRepository.findByEmailIgnoreCase(email);

        List<Payment> payments = paymentRepository.findAllByEmployeeOrderByPeriodDesc(user.getEmail().toLowerCase(Locale.ROOT));

        List<PaymentResponse> paymentResponses = new ArrayList<>();

        for (Payment payment : payments
        ) {
            PaymentResponse paymentResponse = new PaymentResponse(
                    user.getName(),
                    user.getLastname(),
                    payment.getPeriod(),
                    payment.getSalary()
            );
            paymentResponses.add(paymentResponse);

        }
        return paymentResponses;
    }


}
