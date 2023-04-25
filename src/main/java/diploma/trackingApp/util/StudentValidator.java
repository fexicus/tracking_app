package diploma.trackingApp.util;

import diploma.trackingApp.models.Student;
import diploma.trackingApp.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class StudentValidator implements Validator {

    private final StudentService studentService;

    @Autowired
    public StudentValidator(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Student.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Student student = (Student) o;

        //if(studentService.findByUsername(student.getUsername()).isPresent())
        // errors.rejectValue("username", "", "Студент с таким именем пользователя уже занят");

    }
}
