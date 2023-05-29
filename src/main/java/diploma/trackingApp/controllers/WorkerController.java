package diploma.trackingApp.controllers;

import diploma.trackingApp.models.*;
import diploma.trackingApp.services.StudentService;
import diploma.trackingApp.services.TaskService;
import diploma.trackingApp.services.UserService;
import diploma.trackingApp.services.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/worker")
public class WorkerController {
    private final WorkerService workerService;
    private final UserService userService;
    private final TaskService taskService;

    private final StudentService studentService;

    @Autowired
    public WorkerController(WorkerService workerService, UserService userService, TaskService taskService, StudentService studentService) {
        this.workerService = workerService;
        this.userService = userService;
        this.taskService = taskService;
        this.studentService = studentService;
    }

    @PostMapping("/main/updateTaskStatuses")
    public String updateTaskStatusesForWorkers() {
        taskService.updateTaskStatuses();
        return "redirect:/worker/main";
    }


    @GetMapping("/main")
    public String workerMain(@RequestParam(name = "title", required = false) String title,
                             @RequestParam(name = "interests", required = false) String importance,
                             Model model, Authentication authentication){
        String username = authentication.getName();
        Optional<User> optionalUser = userService.findByEmail(username);

        taskService.updateTaskStatuses(); // Обновление статусов перед отображением страницы

        List<Task> tasks;

        if (importance != null && !importance.isEmpty()) {
            Interest selectedImportance = Interest.valueOf(importance);
            tasks = taskService.findAllWithResearchAndImportance(title, selectedImportance);
        } else {
            tasks = taskService.findAllWithResearch(title);
        }

        // Фильтрация задач по статусу "IN_PROGRESS" и связанных со студентом
        List<Task> inProgressTasks = tasks.stream()
                .filter(task -> task.getTaskStatuses().contains(TaskStatus.IN_PROGRESS) &&
                        task.getWorkers().contains(optionalUser.get().getWorker()))
                .toList();


        model.addAttribute("tasks", inProgressTasks); // Используем отфильтрованный список задач
        model.addAttribute("worker", workerService.findOne(optionalUser.get().getWorker().getId()));
        return "worker/main";
    }

    @PostMapping("/task/createdForStudents/updateTaskStatuses")
    public String updateTaskStatusesForWorkersFromStudents() {
        taskService.updateTaskStatuses();
        return "redirect:/worker/task/createdForStudents";
    }

    @GetMapping("/task/createdForStudents")
    public String ShowTasksForStudents(@RequestParam(name = "title", required = false) String title,
                             @RequestParam(name = "interests", required = false) String importance,
                             Model model, Authentication authentication){
        String username = authentication.getName();
        Optional<User> optionalUser = userService.findByEmail(username);

        taskService.updateTaskStatuses(); // Обновление статусов перед отображением страницы

        List<Task> tasks;

        if (importance != null && !importance.isEmpty()) {
            Interest selectedImportance = Interest.valueOf(importance);
            tasks = taskService.findAllWithResearchAndImportance(title, selectedImportance);
        } else {
            tasks = taskService.findAllWithResearch(title);
        }

        String currentUserFirstName = optionalUser.get().getWorker().getName();
        String currentUserLastName = optionalUser.get().getWorker().getSurname();
        String currentUserPatronymic = optionalUser.get().getWorker().getPatronymic();

// Формирование полного имени текущего пользователя
        String currentUserFullName = currentUserLastName + " "  + currentUserFirstName + " " + currentUserPatronymic;

// Фильтрация задач по создателю и статусу "IN_PROGRESS"
        List<Task> inProgressTasks = tasks.stream()
                .filter(task -> task.getCreator().equals(currentUserFullName) && task.getTaskStatuses().contains(TaskStatus.IN_PROGRESS))
                .toList();

        model.addAttribute("tasks", inProgressTasks);


        model.addAttribute("tasks", inProgressTasks); // Используем отфильтрованный список задач
        model.addAttribute("worker", workerService.findOne(optionalUser.get().getWorker().getId()));
        return "task/show_tasks_for_students_from_workers";
    }

    @GetMapping("/{id}")
    public String showOneWorker(@PathVariable("id") int id, Model model){
        model.addAttribute("worker", workerService.findOne(id));
        model.addAttribute("user", userService.findById(workerService.findOne(id).getWorkUser().getId()));
        return "worker/about";
    }

    //добавление новой задачи в список всех задач для студентов
    @GetMapping("/task/newForStudent")
    public String newTaskForStudents(Model model, Authentication authentication){
        String username = authentication.getName();
        Optional<User> optionalUser = userService.findByEmail(username);
        model.addAttribute("worker", workerService.findOne(optionalUser.get().getWorker().getId()));
        model.addAttribute("task", new Task());
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("interests", Interest.values());
        return "task/new_from_worker";
    }

    //POST-метод для внесения новой задачи для студентов
    @PostMapping("/main")
    public String createTaskForStudents(@ModelAttribute("task") Task task, @RequestParam("student") int studentId,
                                        @RequestParam(value = "all", required = false) Boolean allStudents,
                                        @RequestParam("interest") Interest interest, Authentication authentication){
        String username = authentication.getName();
        Optional<User> optionalUser = userService.findByEmail(username);
        if (allStudents != null && allStudents) {
            task.setTaskStatuses(Collections.singleton(TaskStatus.IN_PROGRESS));
            task.setInterests(Collections.singleton(interest)); // Установка значения Interests в Task
            task.setColorOfTask("green");
            task.setCreator(String.format("%s %s %s", optionalUser.get().getWorker().getSurname(), optionalUser.get().getWorker().getName(), optionalUser.get().getWorker().getPatronymic()));
            task.setStartTask(new Date());
            taskService.save(task);

            List<Student> students = studentService.findAll();
            for (Student student : students) {
                studentService.addTask(student, task);
            }
        }
        else if (studentId != 0){
            Student student = studentService.findOne(studentId);
            task.setStudents(Collections.singletonList(student));
            task.setTaskStatuses(Collections.singleton(TaskStatus.IN_PROGRESS));
            task.setInterests(Collections.singleton(interest)); // Установка значения Interests в Task
            task.setColorOfTask("green");
            task.setCreator(String.format("%s %s %s", optionalUser.get().getWorker().getSurname(), optionalUser.get().getWorker().getName(), optionalUser.get().getWorker().getPatronymic()));
            task.setStartTask(new Date());
            taskService.save(task);
            studentService.addTask(student, task);
        }
        return "redirect:/worker/main";
    }
}
