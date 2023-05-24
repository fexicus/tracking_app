package diploma.trackingApp.models;

import diploma.trackingApp.config.PasswordConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "usr")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Почта не должна быть пустой")
    @Pattern(regexp = "^[A-Z]{3}.*@education\\.mai$", message = "Неверно введена почта")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "Пароль пользователя не должен быть пустым")
    @PasswordConstraint
    @Column(name = "password")
    private String password;


    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToOne(mappedBy = "studUser")
    private Student student;

    @OneToOne(mappedBy = "workUser")
    private Worker worker;

    public User(){}

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

}
