package diploma.trackingApp.controllers;

import diploma.trackingApp.models.*;
import diploma.trackingApp.services.*;
import diploma.trackingApp.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private final UserService userService;
    private final StudentService studentService;
    private final WorkerService workerService;
    private final TaskService taskService;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(AdminService adminService, UserService userService, StudentService studentService, WorkerService workerService, TaskService taskService, UserValidator userValidator) {
        this.adminService = adminService;
        this.userService = userService;
        this.studentService = studentService;
        this.workerService = workerService;
        this.taskService = taskService;
        this.userValidator = userValidator;
    }

    //вывод главной страницы для администратора
    @GetMapping("/main")
    public String mainAdmin( @RequestParam(name = "title", required = false) String title, Model model){
        model.addAttribute("tasks", taskService.findAllWithResearch(title));
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
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors())
            return "student/new";
        userService.saveForStudent(user);
        student.setStudUser(user);
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
    public String deleteStudent(@PathVariable ("id") int id) {
        Student student = studentService.findOne(id);
        User user = student.getStudUser();
        int userId = user.getId();
        studentService.delete(id);
        userService.delete(userId);
        return "redirect:/admin/showStudents";
    }
    //--------------------------------П Р Е П О Д А В А Т Е Л И------------------------------------------
    //показ всех преподавателей в вузе - необходимо сделать выпадающий список для групп и т.д.
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

    //добавление нового преподавателя в список всех преподавателей
    @GetMapping("/worker/new")
    public String newWorker(@ModelAttribute("worker") Worker worker, @ModelAttribute("user") User user){
        return "worker/new";
    }

    //POST-метод для внесения информации о новом преподавателей
    @PostMapping("/showWorkers")
    public String createWorker(@ModelAttribute("worker") @Valid Worker worker,
                               @ModelAttribute("user") User user, BindingResult bindingResult){
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors())
            return "worker/new";
        userService.saveForWorker(user);
        worker.setWorkUser(user);
        workerService.save(worker);
            return "redirect:/admin/showWorkers";
    }

    //GET-метод просматривания страницы изменения информации у преподавателя
    @GetMapping("worker/{id}/edit")
    public String editWorker(@PathVariable("id") int id, Model model){
        model.addAttribute("worker", workerService.findOne(id));
        return "worker/edit";
    }

    //PATCH-метод изменения информации о каждом преподавателе
    @PatchMapping("worker/{id}")
    public String updateWorker(@ModelAttribute("worker") @Valid Worker worker,
                               @PathVariable("id") int id){
        workerService.update(id, worker);
        return "redirect:/admin/showWorkers";
    }

    //DELETE-метод удаления преподавателей из базы данных и вообще
    @DeleteMapping("/worker/{id}")
    public String deleteWorker(@PathVariable ("id") int id){
        Worker worker = workerService.findOne(id);
        User user = worker.getWorkUser();
        int userId = user.getId();
        workerService.delete(id);
        userService.delete(userId);

        return "redirect:/admin/showWorkers";
    }
    //----------------------------------------З А Д А Ч И--------------------------------------------------
    //показ всех задач в вузе - необходимо сделать выпадающий список для кого ориентированы, поиск по названию и т.д.
    @GetMapping("/task/{id}")
    public String showOneTask(@PathVariable("id") int id, Model model){
        model.addAttribute("task", taskService.findOne(id));
        return "task/show_one";
    }

    //добавление новой задачи в список всех задач для студентов
    @GetMapping("/task/newForStudent")
    public String newTaskForStudents(Model model){
        model.addAttribute("task", new Task());
        model.addAttribute("students", studentService.findAll());
        return "task/new_for_student";
    }

    //POST-метод для внесения новой задачи для студентов
    @PostMapping("/main")
    public String createTaskForStudents(@ModelAttribute("task") Task task, @RequestParam("student") int studentId,
                                        @RequestParam(value = "all", required = false) Boolean allStudents){
        if (allStudents != null && allStudents) {
            task.setTaskStatuses(Collections.singleton(TaskStatus.IN_PROGRESS));
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
            task.setStartTask(new Date());
            taskService.save(task);
            studentService.addTask(student, task);
        }
        return "redirect:/admin/main";
    }

    //добавление новой задачи в список всех задач для преподавателей
    @GetMapping("/task/newForWorker")
    public String newTaskForWorkers(Model model){
        model.addAttribute("task", new Task());
        model.addAttribute("workers", workerService.findAll());
        return "task/new_for_worker";
    }

    //POST-метод для внесения новой задачи для преподавателей
    @PostMapping("/")
    public String createTaskForWorkers(@ModelAttribute("task") Task task, @RequestParam("worker") int workerId,
                                        @RequestParam(value = "all", required = false) Boolean allWorkers){
        if (allWorkers != null && allWorkers) {
            task.setTaskStatuses(Collections.singleton(TaskStatus.IN_PROGRESS));
            task.setStartTask(new Date());
            taskService.save(task);

            List<Worker> workers = workerService.findAll();
            for (Worker worker : workers) {
                workerService.addTask(worker, task);
            }
        }
        else if (workerId != 0){
            Worker worker = workerService.findOne(workerId);
            task.setWorkers(Collections.singletonList(worker));
            task.setTaskStatuses(Collections.singleton(TaskStatus.IN_PROGRESS));
            task.setStartTask(new Date());
            taskService.save(task);
            workerService.addTask(worker, task);
        }
        return "redirect:/admin/main";
    }

    //GET-метод просматривания страницы изменения информации у задачи
    @GetMapping("task/{id}/edit")
    public String editTask(@PathVariable("id") int id, Model model){
        model.addAttribute("task", taskService.findOne(id));
        return "task/edit";
    }

    //PATCH-метод изменения информации о каждой с задаче
    @PatchMapping("task/{id}")
    public String updateTask(@ModelAttribute("task") @Valid Task task,
                             @PathVariable("id") int id){
        taskService.update(id, task);
        return "redirect:/admin/main";
    }

    //DELETE-метод удаления задач из базы данных и вообще ото всюду
    @DeleteMapping("/task/{id}")
    public String deleteTask(@PathVariable ("id") int id){
        taskService.delete(id);
        return "redirect:/admin/main";
    }
}