package diploma.trackingApp.controllers;

import diploma.trackingApp.models.Student;
import diploma.trackingApp.models.Task;
import diploma.trackingApp.models.User;
import diploma.trackingApp.models.Worker;
import diploma.trackingApp.services.*;
import diploma.trackingApp.util.StudentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private final UserService userService;
    private final StudentService studentService;
    private final WorkerService workerService;
    private final TaskService taskService;
    private final StudentValidator studentValidator;

    @Autowired
    public AdminController(AdminService adminService, UserService userService, StudentService studentService, WorkerService workerService, TaskService taskService, StudentValidator studentValidator) {
        this.adminService = adminService;
        this.userService = userService;
        this.studentService = studentService;
        this.workerService = workerService;
        this.taskService = taskService;
        this.studentValidator = studentValidator;
    }

    //вывод главной страницы для администратора
    @GetMapping("/main")
    public String admHello(Model model){
        model.addAttribute("tasks", taskService.findAll());
        return "admin/main";
    }

    //показ информации администратора
    @GetMapping("/showAdminInfo/{id}")
    public String showAdminInfo(@PathVariable("id") int id, Model model){
        model.addAttribute("admin", adminService.findOne(id));
        return "admin/show_info";
    }

    //--------------------------------------С Т У Д Е Н Т Ы------------------------------------------------
    //показ всех студентов в вузе - необходимо сделать выпадающий список для курсов и т.д.
    @GetMapping("/showStudents")
    public String showStudents(Model model){
        //List<Student> students = studentService.findStudentsByCourse(course);
        //@RequestParam("course") String course,
        //model.addAttribute("students", students);
        model.addAttribute("students", studentService.findAll());
        return "student/showAll";
    }

    //вывод информации каждого студента более подробно
    @GetMapping("/student/{id}")
    public String showOneStudent(@PathVariable("id") int id, Model model){
        model.addAttribute("student", studentService.findOne(id));
        return "student/show_one";
    }

    //добавление нового студента в список всех студентов
    @GetMapping("/student/new")
    public String newStudent(@ModelAttribute("student") Student student, @ModelAttribute("user") User user){
        return "student/new";
    }

    //POST-метод для внесения информации о новом студенте
    @PostMapping("/showStudents")
    public String createStudent(@ModelAttribute("student") @Valid Student student,
                                @ModelAttribute("user") User user,
                                BindingResult bindingResult){
        //studentValidator.validate(student, bindingResult);
        //if(bindingResult.hasErrors())
          //  return "student/new";
        userService.saveForStudent(user);
        studentService.save(student);
        return "redirect:/admin/showStudents";
    }

    //GET-метод просматривания страницы изменения информации у студента
    @GetMapping("student/{id}/edit")
    public String editStudent(@PathVariable("id") int id, Model model){
        model.addAttribute("student", studentService.findOne(id));
        return "student/edit";
    }

    //PATCH-метод изменения информации о каждом с студенте
    @PatchMapping("student/{id}")
    public String updateStudent(@ModelAttribute("student") @Valid Student student, @PathVariable("id") int id){
        studentService.update(id, student);
        return "redirect:/admin/showStudents";
    }

    //DELETE-метод удаления студентов из базы данных и из приложения в целом
    @DeleteMapping("/student/{id}")
    public String deleteStudent(@PathVariable ("id") int id){
        userService.delete(id);
        studentService.delete(id);
        return "redirect:/admin/showStudents";

        /*User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin/showStudents";
        }
        List<Student> students = studentService.findByUser(user);
        for (Student student : students) {
            studentService.delete(student.getId());
        }
        userService.delete(id);
        return "redirect:/admin/showStudents";*/
    }
    //--------------------------------П Р Е П О Д А В А Т Е Л И------------------------------------------
    @GetMapping("/showWorkers")
    public String showWorkers(Model model){
        model.addAttribute("workers", workerService.findAll());
        return "worker/showAll";
    }

    //вывод информации каждого преподавателя более подробно
    @GetMapping("/worker/{id}")
    public String showOneWorker(@PathVariable("id") int id, Model model){
        model.addAttribute("worker", workerService.findOne(id));
        return "worker/show_one";
    }

    //добавление нового студента в список всех студентов
    @GetMapping("/worker/new")
    public String newWorker(@ModelAttribute("worker") Worker worker, @ModelAttribute("user") User user){
        return "worker/new";
    }

    //POST-метод для внесения информации о новом студенте
    @PostMapping("/showWorkers")
    public String createWorker(@ModelAttribute("worker") @Valid Worker worker,
                               @ModelAttribute("user") User user){
        //studentValidator.validate(student, bindingResult);
        //if(bindingResult.hasErrors())
        // return "admin/student_new";
        userService.saveForWorker(user);
        workerService.save(worker);
        return "redirect:/admin/showWorkers";
    }

    //GET-метод просматривания страницы изменения информации у студента
    @GetMapping("worker/{id}/edit")
    public String editWorker(@PathVariable("id") int id, Model model){
        model.addAttribute("worker", workerService.findOne(id));
        return "worker/edit";
    }

    //PATCH-метод изменения информации о каждом с студенте
    @PatchMapping("worker/{id}")
    public String updateWorker(@ModelAttribute("worker") @Valid Worker worker,
                               @PathVariable("id") int id){
        workerService.update(id, worker);
        return "redirect:/admin/showWorkers";
    }

    //DELETE-метод удаления студентов из базы данных и вообще
    @DeleteMapping("/worker/{id}")
    public String deleteWorker(@PathVariable ("id") int id){
        workerService.delete(id);
        return "redirect:/admin/showWorkers";


    }
    //----------------------------------------З А Д А Ч И--------------------------------------------------
    @GetMapping("/task/{id}")
    public String showOneTask(@PathVariable("id") int id, Model model){
        model.addAttribute("task", taskService.findOne(id));
        return "task/show_one";
    }

    //добавление нового студента в список всех студентов
    @GetMapping("/task/new")
    public String newTask(@ModelAttribute("task") Task task){
        return "task/new";
    }

    //POST-метод для внесения информации о новом студенте
    @PostMapping("/main")
    public String createTask(@ModelAttribute("task") @Valid Task task){
        //studentValidator.validate(student, bindingResult);
        //if(bindingResult.hasErrors())
        // return "admin/student_new";
        task.setStartTask(new Date());
        taskService.save(task);
        return "redirect:/admin/main";
    }

    //GET-метод просматривания страницы изменения информации у студента
    @GetMapping("task/{id}/edit")
    public String editTask(@PathVariable("id") int id, Model model){
        model.addAttribute("task", taskService.findOne(id));
        return "task/edit";
    }

    //PATCH-метод изменения информации о каждом с студенте
    @PatchMapping("task/{id}")
    public String updateTask(@ModelAttribute("task") @Valid Task task,
                             @PathVariable("id") int id){
        taskService.update(id, task);
        return "redirect:/admin/main";
    }

    //DELETE-метод удаления студентов из базы данных и вообще
    @DeleteMapping("/task/{id}")
    public String deleteTask(@PathVariable ("id") int id){
        taskService.delete(id);
        return "redirect:/admin/main";
    }
}
