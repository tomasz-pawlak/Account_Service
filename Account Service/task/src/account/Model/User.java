package account.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    public User(Long id, String name, String lastname, String email, String password) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotEmpty
    private String name;

    @NotBlank
    @NotEmpty
    private String lastname;

    @NotBlank
    @NotEmpty
    @Pattern(regexp = ".+@acme.com")
    private String email;

    //    @NotBlank
    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 12)
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payment> payments;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean accountNonLocked = true;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int failedAttempt;

    @ElementCollection(fetch = FetchType.EAGER)
    @SortNatural
    private Set<String> roles = new TreeSet<>();

    public void addUserRole(String role) {
        roles.add(role);
    }

    public void removeUserRole(String role) {
        roles.remove(role);
    }

    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", payments=" + payments +
                ", accountNonLocked=" + accountNonLocked +
                ", failedAttempt=" + failedAttempt +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
