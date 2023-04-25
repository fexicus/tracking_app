package diploma.trackingApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentController {
    //вывод главной страницы для администратора
    @GetMapping("/main")
    public String admHello(Model model){
        //model.addAttribute("tasks", taskService.findAll());
        return "student/main";
    }
}
