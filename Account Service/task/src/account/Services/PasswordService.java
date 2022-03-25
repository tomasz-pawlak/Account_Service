package account.Services;

import account.Model.Password;
import account.Model.User;
import account.Exceptions.NotSavePasswordException;
import account.Exceptions.SamePasswordException;
import account.Exceptions.ShortPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordService {

    @Autowired
    PasswordEncoder encoder;

    public List<String> hackedPassword = List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch",
            "PasswordForApril", "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");

    public void checkMinChars(Password password) {
        if (password.getNew_password().length() < 12) {
            System.out.println("test");
            throw new ShortPasswordException();
        }
    }

    public void checkDuplicate(User user, Password password) {
        if (encoder.matches(password.getNew_password(), user.getPassword())) {
            throw new SamePasswordException();
        }
    }

    public void checkHackedList(Password password) {
        if (hackedPassword.contains(password.getNew_password())) {
            throw new NotSavePasswordException();
        }
    }

    public void checkPassword(User user, Password password){
        checkMinChars(password);
        checkDuplicate(user,password);
        checkHackedList(password);
    }


}
