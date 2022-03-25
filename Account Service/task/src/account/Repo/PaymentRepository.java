package account.Repo;

import account.Model.Payment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {

    boolean existsByEmployeeAndPeriod(String employee, String period);

    Payment findPaymentByEmployeeAndPeriod(String employee, String period);

    List<Payment> findAllByEmployeeOrderByPeriodDesc(String email);

    @Transactional
    @Modifying
    @Query("update Payment p set p.salary = :salary WHERE p.employee = :employee and p.period = :period")
    void update(@Param("employee") String employee, @Param("period") String period, @Param("salary") Long salary);


}
