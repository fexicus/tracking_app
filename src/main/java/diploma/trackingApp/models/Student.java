package diploma.trackingApp.models;

import lombok.Getter;
import lombok.Setter;

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
@Table(name = "Student")
public class Student {
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
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов длиной")
    private String patronymic;

    @Column(name = "nameofgroup")
    @NotEmpty(message = "Номер группы пользователя не должен быть пустой")
    @Size(max = 15, message = "Номер группы должен быть до 50 символов длиной")
    //вставить регулярное выражение для группы
    private String nameOfGroup;

    @Column(name = "course")
    @NotEmpty
    private String course;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User studUser;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "students_tasks",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    private List<Task> tasks = new ArrayList<>();


    public Student(){}

    public Student(int id, String name, String surname, String patronymic, String nameOfGroup, String course) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.nameOfGroup = nameOfGroup;
        this.course = course;
    }
}