package diploma.trackingApp.services;

import diploma.trackingApp.models.*;
import diploma.trackingApp.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;

    private final StudentService studentService;

    private final WorkerService workerService;


    public TaskService(TaskRepository taskRepository, StudentService studentService, WorkerService workerService) {
        this.taskRepository = taskRepository;
        this.studentService = studentService;
        this.workerService = workerService;
    }


    public List<Task> findAllWithResearch(String title) {
        if (title != null) {
            return taskRepository.findByTitle(title);
        } else {
            return taskRepository.findAll();
        }
    }

    public Task findOne(int id){
        Optional<Task> foundTask = taskRepository.findById(id);
        return foundTask.orElse(null);
    }

    @Transactional
    public void save(Task task){

        taskRepository.save(task);
    }

    @Transactional
    public void createTaskForStudents(Task task, int studentId, Boolean allStudents, Interest interest) {
        task.setTaskStatuses(Collections.singleton(TaskStatus.IN_PROGRESS));
        task.setInterests(Collections.singleton(interest));
        task.setColorOfTask("green");
        task.setCreator("Администрация");
        task.setStartTask(new Date());

        if (allStudents != null && allStudents) {
            saveTaskForAllStudents(task);
        } else if (studentId != 0) {
            saveTaskForStudent(task, studentId);
        }
    }
    @Transactional
    public void saveTaskForAllStudents(Task task) {
        List<Student> students = studentService.findAll();
        for (Student student : students) {
            addTaskToStudent(student, task);
        }
        taskRepository.save(task);
    }
    @Transactional
    public void saveTaskForStudent(Task task, int studentId) {
        Student student = studentService.findOne(studentId);
        if (student != null) {
            task.setStudents(Collections.singletonList(student));
            addTaskToStudent(student, task);
            taskRepository.save(task);
        }
    }
    @Transactional
    public void addTaskToStudent(Student student, Task task) {
        studentService.addTask(student, task);
    }


    @Transactional
    public void update(int id, Task updatedTask){
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setTitle(updatedTask.getTitle());
            task.setFullText(updatedTask.getFullText());
            task.setEndTask(updatedTask.getEndTask());
            task.setStartTask(updatedTask.getStartTask());
            task.setTaskStatuses(updatedTask.getTaskStatuses());
            // Обновление других свойств задачи, кроме важности
            taskRepository.save(task);
        }
    }


    @Transactional
    public void delete(int id){
        taskRepository.deleteById(id);
    }

    public List<Task> findAllWithResearchAndImportance(String title, Interest importance) {

        if (title != null && !title.isEmpty() && importance != null) {
            return taskRepository.findByTitleContainingAndInterests(title, importance);
        } else if (title != null && !title.isEmpty()) {
            return taskRepository.findByTitleContaining(title);
        } else if (importance != null) {
            return taskRepository.findByInterests(importance);
        } else {
            return taskRepository.findAll();
        }
    }

    @Transactional
    public void updateTaskStatuses() {
        List<Task> tasks = taskRepository.findAll();

        for (Task task : tasks) {
            if (task.getTaskStatuses().contains(TaskStatus.IN_PROGRESS) && isTaskExpired(task)) {
                task.getTaskStatuses().remove(TaskStatus.IN_PROGRESS);
                task.getTaskStatuses().add(TaskStatus.COMPLETED);
                taskRepository.save(task); // Сохраняем изменения в базе данных
            } else if (!task.getTaskStatuses().contains(TaskStatus.IN_PROGRESS) && !isTaskExpired(task)) {
                task.getTaskStatuses().add(TaskStatus.IN_PROGRESS);
                taskRepository.save(task); // Сохраняем изменения в базе данных
            }
        }
    }
    private boolean isTaskExpired(Task task) {
        Date currentDate = new Date();
        Date endTaskDate = task.getEndTask();

        return endTaskDate != null && endTaskDate.before(currentDate);
    }

    public List<Task> findAllCompletedTasks() {
        List<Task> allTasks = taskRepository.findAll();
        List<Task> completedTasks = new ArrayList<>();

        for (Task task : allTasks) {
            if (task.getTaskStatuses().contains(TaskStatus.COMPLETED)) {
                completedTasks.add(task);
            }
        }

        return completedTasks;
    }
    @Transactional
    public void createTaskForWorkers(Task task, int workerId, Boolean allWorkers, Interest interest) {
        task.setTaskStatuses(Collections.singleton(TaskStatus.IN_PROGRESS));
        task.setInterests(Collections.singleton(interest));
        task.setColorOfTask("yellow");
        task.setCreator("Администрация");
        task.setStartTask(new Date());

        if (allWorkers != null && allWorkers) {
            saveTaskForAllWorkers(task);
        } else if (workerId != 0) {
            saveTaskForWorker(task, workerId);
        }
    }
    @Transactional
    public void saveTaskForAllWorkers(Task task) {
        List<Worker> workers = workerService.findAll();
        for (Worker worker : workers) {
            addTaskToWorker(worker, task);
        }
        taskRepository.save(task);
    }
    @Transactional
    public void saveTaskForWorker(Task task, int workerId) {
        Worker worker = workerService.findOne(workerId);
        if (worker != null) {
            task.setWorkers(Collections.singletonList(worker));
            addTaskToWorker(worker, task);
            taskRepository.save(task);;
        }
    }
    @Transactional
    public void addTaskToWorker(Worker worker, Task task) {
        workerService.addTask(worker, task);
    }


}
