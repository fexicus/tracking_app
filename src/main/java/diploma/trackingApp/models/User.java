package diploma.trackingApp.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private Set<Role> roles;

    @OneToMany
    @JoinColumn(name = "admUser")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Admin> admins;

    @OneToMany
    @JoinColumn(name = "studUser")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Student> students;

    @OneToMany
    @JoinColumn(name = "workUser")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Worker> workers;

    public User(){}

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

}
