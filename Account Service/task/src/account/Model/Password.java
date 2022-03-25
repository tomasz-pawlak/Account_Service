package account.Model;

import lombok.*;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Password {

    @NotNull
    private String new_password;

}
