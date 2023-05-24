package diploma.trackingApp.controllers;

import diploma.trackingApp.models.Interest;
import diploma.trackingApp.models.Task;
import diploma.trackingApp.models.TaskStatus;
import diploma.trackingApp.models.User;
import diploma.trackingApp.services.StudentService;
import diploma.trackingApp.services.TaskService;
import diploma.trackingApp.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final TaskService taskService;

    private final UserService userService;

    public StudentController(StudentService studentService, TaskService taskService, UserService userService) {
        this.studentService = studentService;
        this.taskService = taskService;
        this.userService = userService;
    }

    //вывод главной страницы для администратора
    /*@GetMapping("/main")
    public String studMain(@RequestParam(name = "title", required = false) String title,
                           @RequestParam(name = "interests", required = false) String importance,
                           Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> optionalUser = userService.findByEmail(username);

        model.addAttribute("tasks", taskService.findByStudentsId(optionalUser.get().getStudent().getId()));
        model.addAttribute("student", studentService.findOne(optionalUser.get().getStudent().getId()));
        return "student/main";
    }*/

    @PostMapping("/main/updateTaskStatuses")
    public String updateTaskStatusesForStudent() {
        taskService.updateTaskStatuses();
        return "redirect:/student/main";
    }
    @GetMapping("/main")
    public String studMain(@RequestParam(name = "title", required = false) String title,
                           @RequestParam(name = "interests", required = false) String importance,
                           Model model, Authentication authentication) {
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
                        task.getStudents().stream()
                                .anyMatch(student -> student.equals(optionalUser.get().getStudent())))
                .toList();


        model.addAttribute("tasks", inProgressTasks); // Используем отфильтрованный список задач
        model.addAttribute("student", studentService.findOne(optionalUser.get().getStudent().getId()));
        return "student/main";
    }

    /*@GetMapping("/completed")
    public String showFinishedTasksForStudents(Model model, Authentication authentication){
        String username = authentication.getName();
        Optional<User> optionalUser = userService.findByEmail(username);

        taskService.updateTaskStatuses();

        // Получение всех завершенных задач
        List<Task> completedTasks = taskService.findAllCompletedTasks();

        // Добавление завершенных задач в модель
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("student", studentService.findOne(optionalUser.get().getStudent().getId()));

        return "student/completed";
    }*/

    @GetMapping("/completed")
    public String showFinishedTasksForStudents(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> optionalUser = userService.findByEmail(username);

        taskService.updateTaskStatuses();

        // Получение всех завершенных задач
        List<Task> completedTasks = taskService.findAllCompletedTasks();

        // Фильтрация завершенных задач по студенту
        List<Task> completedTasksForStudent = completedTasks.stream()
                .filter(task -> task.getStudents().contains(optionalUser.get().getStudent()))
                .toList();

        model.addAttribute("completedTasks", completedTasksForStudent);
        model.addAttribute("student", studentService.findOne(optionalUser.get().getStudent().getId()));

        return "student/completed";
    }


    @GetMapping("/{id}")
    public String showOneStudent(@PathVariable("id") int id, Model model){
        model.addAttribute("student", studentService.findOne(id));
        model.addAttribute("user", userService.findById(studentService.findOne(id).getStudUser().getId()));
        return "student/about";
    }

    @GetMapping("/task/{id}")
    public String showOneTask(@PathVariable("id") int id, Model model){
        model.addAttribute("task", taskService.findOne(id));
        return "task/show_one";
    }
}
