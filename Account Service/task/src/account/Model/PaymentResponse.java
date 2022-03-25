package account.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentResponse {

    private String name;
    private String lastname;
    private String period;
    private String salary;


    public PaymentResponse(String name, String lastname, String period, Long salary) {
        this.name = name;
        this.lastname = lastname;
        this.period = convertPeriod(period);
        this.salary = convertSalary(salary);
    }

    public String convertPeriod(String period) {
        String prefix = period.substring(0, 2);
        String suffix = period.substring(2);
        String result = null;
        switch (prefix) {
            case "01":
                result = "January" + suffix;
                break;
            case "02":
                result = "February" + suffix;
                break;
            case "03":
                result = "March" + suffix;
                break;
            case "04":
                result = "April" + suffix;
                break;
            case "05":
                result = "May" + suffix;
                break;
            case "06":
                result = "June" + suffix;
                break;
            case "07":
                result = "July" + suffix;
                break;
            case "08":
                result = "August" + suffix;
                break;
            case "09":
                result = "September" + suffix;
                break;
            case "10":
                result = "October" + suffix;
                break;
            case "11":
                result = "November" + suffix;
                break;
            case "12":
                result = "December" + suffix;
                break;
            default:
                throw new RuntimeException("Problems with period!");
        }
        return result;
    }

    public String convertSalary(Long salary) {
        Long dollar = salary / 100;
        Long cent = salary % 100;

        return dollar + " dollar(s) "+ cent+ " cent(s)";

    }
}
