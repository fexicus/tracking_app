package diploma.trackingApp.controllers;

import diploma.trackingApp.models.User;
import diploma.trackingApp.services.TaskService;
import diploma.trackingApp.services.UserService;
import diploma.trackingApp.services.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/worker")
public class WorkerController {
    private final WorkerService workerService;
    private final UserService userService;
    private final TaskService taskService;

    @Autowired
    public WorkerController(WorkerService workerService, UserService userService, TaskService taskService) {
        this.workerService = workerService;
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/main")
    public String workerMain(Model model, Authentication authentication){
        String username = authentication.getName();
        Optional<User> optionalUser = userService.findByEmail(username);
        model.addAttribute("tasks", taskService.findByWorkersId(optionalUser.get().getWorker().getId()));
        model.addAttribute("worker", workerService.findOne(optionalUser.get().getWorker().getId()));
        return "worker/main";
    }

    @GetMapping("/{id}")
    public String showOneWorker(@PathVariable("id") int id, Model model){
        model.addAttribute("worker", workerService.findOne(id));
        model.addAttribute("user", userService.findById(workerService.findOne(id).getWorkUser().getId()));
        return "worker/about";
    }
}
