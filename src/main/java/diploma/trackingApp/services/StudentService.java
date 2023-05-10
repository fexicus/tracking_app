package diploma.trackingApp.services;

import diploma.trackingApp.models.Student;
import diploma.trackingApp.models.Task;
import diploma.trackingApp.models.User;
import diploma.trackingApp.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
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
    public List<Student> findByUser(User user) {
        return studentRepository.findByStudUser(user);
    }
    @Transactional
    public List<Student> findByStudUserEmail(String email){
        return studentRepository.findByStudUserEmail(email);
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
    public void addTasks(List<Student> students, List<Task> tasks) {
        if (students == null || tasks == null) {
            throw new IllegalArgumentException("Students and tasks cannot be null");
        }
        if (students.isEmpty() || tasks.isEmpty()) {
            throw new IllegalArgumentException("Students and tasks cannot be empty");
        }
        for (Student student : students) {
            for (Task task : tasks) {
                addTask(student, task);
            }
        }
    }

}
