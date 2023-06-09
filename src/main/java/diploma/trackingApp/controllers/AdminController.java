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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final StudentService studentService;
    private final WorkerService workerService;
    private final TaskService taskService;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserService userService, StudentService studentService, WorkerService workerService, TaskService taskService, UserValidator userValidator) {
        this.userService = userService;
        this.studentService = studentService;
        this.workerService = workerService;
        this.taskService = taskService;
        this.userValidator = userValidator;
    }

    @PostMapping("/main/updateTaskStatuses")
    public String updateTaskStatuses() {
        taskService.updateTaskStatuses();
        return "redirect:/admin/main";
    }

    @GetMapping("/main")
    public String mainAdmin(@RequestParam(name = "title", required = false) String title,
                            @RequestParam(name = "interests", required = false) String importance,
                            Model model) {
        taskService.updateTaskStatuses(); // Обновление статусов перед отображением страницы

        List<Task> tasks;

        if (importance != null && !importance.isEmpty()) {
            Interest selectedImportance = Interest.valueOf(importance);
            tasks = taskService.findAllWithResearchAndImportance(title, selectedImportance);
        } else {
            tasks = taskService.findAllWithResearch(title);
        }

        // Фильтрация задач по статусу "IN_PROGRESS"
        List<Task> inProgressTasks = tasks.stream()
                .filter(task -> task.getTaskStatuses().contains(TaskStatus.IN_PROGRESS))
                .collect(Collectors.toList());

        model.addAttribute("tasks", inProgressTasks);
        model.addAttribute("interests", Interest.values());

        return "admin/main";
    }

    @GetMapping("/completed")
    public String showFinishedTasks(Model model){
        taskService.updateTaskStatuses();

        // Получение всех завершенных задач
        List<Task> completedTasks = taskService.findAllCompletedTasks();

        // Добавление завершенных задач в модель
        model.addAttribute("completedTasks", completedTasks);

        return "admin/completed";
    }

    //--------------------------------------С Т У Д Е Н Т Ы------------------------------------------------

    @GetMapping("/showStudents")
    public String showStudents(@RequestParam(value = "course", required = false) String courseNumber, Model model) {
        List<Student> students;
        if (courseNumber != null && !courseNumber.isEmpty()) {
            students = studentService.findStudentsByCourse(courseNumber);
        } else {
            students = studentService.findAll();
        }
        model.addAttribute("students", students);
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
    public String updateStudent(@ModelAttribute("student") @Valid Student student, @PathVariable("id") int id,
                                @RequestParam("userId") int userId){
        studentService.update(id, student);
        studentService.updateUserId(id, userId);
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
    public String editWorker(@PathVariable("id") int id, Model model, @ModelAttribute("user") User user){
        model.addAttribute("worker", workerService.findOne(id));
        return "worker/edit";
    }

    //PATCH-метод изменения информации о каждом преподавателе
    @PatchMapping("worker/{id}")
    public String updateWorker(@ModelAttribute("worker") @Valid Worker worker,
                               @PathVariable("id") int id,
                               @RequestParam("userId") int userId) {
        workerService.update(id, worker);
        workerService.updateUserId(id, userId);
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
        model.addAttribute("interests", Interest.values()); // Передаем список значений Enum в модель

        return "task/new_for_student";
    }

    //POST-метод для внесения новой задачи для студентов
    @PostMapping("/main")
    public String createTaskForStudents(@ModelAttribute("task") Task task, @RequestParam("student") int studentId,
                                        @RequestParam(value = "all", required = false) Boolean allStudents,
                                        @RequestParam("interest") Interest interest) {
        taskService.createTaskForStudents(task, studentId, allStudents, interest);
        return "redirect:/admin/main";
    }


    //добавление новой задачи в список всех задач для преподавателей
    @GetMapping("/task/newForWorker")
    public String newTaskForWorkers(Model model){
        model.addAttribute("task", new Task());
        model.addAttribute("workers", workerService.findAll());
        model.addAttribute("interests", Interest.values()); // Передаем список значений Enum в модель
        model.addAttribute("departments", workerService.findAllDepartment()); // Список всех кафедр (уникальные значения поля department)
        //model.addAttribute("selectedDepartment", ""); // Выбранная кафедра (по умолчанию пустая строка)
        //model.addAttribute("groups", ""); // Список всех групп
        return "task/new_for_worker";
    }

/*  @ResponseBody
    @GetMapping("/task/workers")
    public List<Worker> getWorkersByDepartment(@RequestParam("department") String department) {
        return workerService.findByDepartment(department);
    }*/


    //POST-метод для внесения новой задачи для преподавателей
    @PostMapping("/")
    public String createTaskForWorkers(@ModelAttribute("task") Task task, @RequestParam("worker") int workerId,
                                       @RequestParam(value = "all", required = false) Boolean allWorkers,
                                       @RequestParam("interest") Interest interest) {
        taskService.createTaskForWorkers(task, workerId, allWorkers, interest);
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