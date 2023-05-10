package diploma.trackingApp.repositories;

import diploma.trackingApp.models.Student;
import diploma.trackingApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Spliterator;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByCourse(String course);
    List<Student> findByStudUser(User user);

    List<Student> findByStudUserEmail(String email);

}
