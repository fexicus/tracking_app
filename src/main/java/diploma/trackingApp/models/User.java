package diploma.trackingApp.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
    //@Size(min = 2, max = 100, message = "Почта должна быть от 2 до 100 символов длиной")
    //добавить регулярное выражение для почты(у умного чела в проекте)
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "Пароль пользователя не должен быть пустым")
    @Column(name = "password")
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToOne
    @JoinColumn(name = "admUser")
    private Admin admin;

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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", student=" + student +
                '}';
    }

}
