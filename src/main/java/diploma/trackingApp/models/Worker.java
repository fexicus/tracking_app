package diploma.trackingApp.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(name = "department")
    @Size(min = 3)
    private String department;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User workUser;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "workers_tasks",
            joinColumns = @JoinColumn(name = "worker_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    private Set<Task> tasksForWorkers = new HashSet<>();

    public void setWorkUser(User user) {
        this.workUser = user;
        user.setWorker(this);
    }

    public Worker(){}

    public Worker(int id, String name, String surname, String patronymic, String subject, String department, User workUser, Set<Task> tasksForWorkers) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.subject = subject;
        this.department = department;
        this.workUser = workUser;
        this.tasksForWorkers = tasksForWorkers;
    }
}
