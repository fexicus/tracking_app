package diploma.trackingApp.services;

import diploma.trackingApp.models.Student;
import diploma.trackingApp.models.Task;
import diploma.trackingApp.models.User;
import diploma.trackingApp.repositories.StudentRepository;
import diploma.trackingApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public StudentService(StudentRepository studentRepository, UserRepository userRepository, UserService userService) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<Student> findAll(){
        return studentRepository.findAll();
    }

    public Student findOne(int id){
        Optional<Student> foundStudent = studentRepository.findById(id);
        return  foundStudent.orElse(null);
    }
    @Transactional
    public void save(Student student){
        studentRepository.save(student);
    }

    @Transactional
    public void update(int id, Student updatedStudent){
        updatedStudent.setId(id);
        studentRepository.save(updatedStudent);
    }

    @Transactional
    public void delete(int id){
        studentRepository.deleteById(id);
    }

    @Transactional

    public List<Student> findStudentsByCourse(String course){
        return studentRepository.findByCourse(course);
    }

    @Transactional
    public void addTask(Student student, Task task) {
        List<Task> tasks = student.getTasks();
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
    }


    @Transactional
    public void updateUserId(int studentId, int userId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found with id: " + studentId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        student.setStudUser(user);
        studentRepository.save(student);
    }

}
