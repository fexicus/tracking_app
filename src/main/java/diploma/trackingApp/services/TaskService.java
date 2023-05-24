package diploma.trackingApp.services;

import diploma.trackingApp.models.Interest;
import diploma.trackingApp.models.Task;
import diploma.trackingApp.models.TaskStatus;
import diploma.trackingApp.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;


    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
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
    public void saveForStatus(Task task) {
        taskRepository.save(task); // Сохраняем задачу

        // Обновляем статусы задач
        if (isTaskExpired(task)) {
            task.getTaskStatuses().remove(TaskStatus.IN_PROGRESS);
            task.getTaskStatuses().add(TaskStatus.COMPLETED);
        } else if (!task.getTaskStatuses().contains(TaskStatus.IN_PROGRESS)) {
            task.getTaskStatuses().add(TaskStatus.IN_PROGRESS);
        }

        taskRepository.save(task); // Сохраняем обновленные статусы задач
    }

/*    @Transactional
    public void update(int id, Task updatedTask){
        updatedTask.setId(id);
        taskRepository.save(updatedTask);
    }*/

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

    @Transactional
    public List<Task> findByStudentsId(int id) {
        return taskRepository.findByStudentsId(id);
    }

    @Transactional
    public List<Task> findByWorkersId(int id) {
        return taskRepository.findByWorkersId(id);
    }

    public List<Task> findAllWithResearchAndImportance(String title, Interest importance) {
        // Perform a database query or any other necessary operations to retrieve tasks based on the given parameters
        // You can combine the title and importance filters in the query or apply them separately as per your requirements

        // Example implementation using a JPA repository
        if (title != null && !title.isEmpty() && importance != null) {
            return taskRepository.findByTitleContainingAndInterests(title, importance);
        } else if (title != null && !title.isEmpty()) {
            return taskRepository.findByTitleContaining(title);
        } else if (importance != null) {
            return taskRepository.findByInterests(importance);
        } else {
            // Return all tasks if no filters are applied
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


}
