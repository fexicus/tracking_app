package diploma.trackingApp.controllers;

import diploma.trackingApp.models.User;
import diploma.trackingApp.models.Worker;
import diploma.trackingApp.services.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/worker")
@PreAuthorize("hasAuthority('ROLE_WORKER')")
public class WorkerController {
    private final WorkerService workerService;

    @Autowired
    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping("/main")
    public String workerMain(Model model){
        model.addAttribute("workers", workerService.findAll());
        return "worker/main";
    }

    //GET-метод просматривания страницы информации преподавателя
    @GetMapping("/worker/{id}")
    public String editWorker(@PathVariable("id") int id, Model model){
        model.addAttribute("worker", workerService.findOne(id));
        return "worker/info";
    }
}
