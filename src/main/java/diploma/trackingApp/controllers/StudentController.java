package diploma.trackingApp.controllers;

import diploma.trackingApp.models.User;
import diploma.trackingApp.services.StudentService;
import diploma.trackingApp.services.TaskService;
import diploma.trackingApp.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;


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
    @GetMapping("/main")
    public String studMain(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> optionalUser = userService.findByEmail(username);
        model.addAttribute("tasks", taskService.findByStudentsId(optionalUser.get().getStudent().getId()));
        model.addAttribute("student", studentService.findOne(optionalUser.get().getStudent().getId()));
        return "student/main";
    }

    @GetMapping("/{id}")
    public String showOneStudent(@PathVariable("id") int id, Model model){
        model.addAttribute("student", studentService.findOne(id));
        model.addAttribute("user", userService.findById(studentService.findOne(id).getStudUser().getId()));
        return "student/about";
    }
}
