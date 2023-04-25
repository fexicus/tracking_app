package diploma.trackingApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/worker")
public class WorkerController {
    @GetMapping("/main")
    public String admHello(Model model){
        //model.addAttribute("tasks", taskService.findAll());
        return "worker/main";
    }
}
