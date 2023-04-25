package diploma.trackingApp.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "Worker")
public class Worker {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Имя пользователя не должно быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов длиной")
    private String name;

    @Column(name = "surname")
    @NotEmpty(message = "Фамилия пользователя не должна быть пустой")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов длиной")
    private String surname;

    @Column(name = "patronymic")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 50 символов длиной")
    private String patronymic;

    @Column(name = "subject")
    @Size(min = 2)
    private String subject;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User workUser;

    public Worker(){}

    public Worker(int id, String name, String surname, String patronymic, String subject, User workUser) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.subject = subject;
        this.workUser = workUser;
    }
}
