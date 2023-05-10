package diploma.trackingApp.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Task")
@Getter
@Setter
public class Task {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "full_text")
    private String fullText;

    @Column(name = "start_task")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date startTask;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_task")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date endTask;

    @ElementCollection(targetClass = TaskStatus.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "task_taskStatus",
            joinColumns = @JoinColumn(name = "task_id"))
    @Enumerated(EnumType.STRING)
    private Set<TaskStatus> taskStatuses;

    @ManyToMany(mappedBy = "tasks")
    private List<Student> students = new ArrayList<>();

    @ManyToMany(mappedBy = "tasksForWorkers")
    private List<Worker> workers = new ArrayList<>();

    @Transient
    private boolean expired;//проверка на просроченность задачи

    public Task(){}

    public Task(int id, String title, String fullText, Date startTask, Date endTask) {
        this.id = id;
        this.title = title;
        this.fullText = fullText;
        this.startTask = startTask;
        this.endTask = endTask;
    }
}
