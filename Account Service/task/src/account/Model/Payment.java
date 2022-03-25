package account.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotEmpty
    @Pattern(regexp = ".+@acme.com")
    private String employee;

    @NotBlank
    @NotEmpty
    @Pattern(regexp = "0[1-9]-\\d{4}|(1[012])-\\d{4}", message = "not match")
    private String period;

    @Min(0)
    private Long salary;

    @ManyToOne
    @JoinColumn(name = "user")
    @JsonIgnore
    User user;

}
