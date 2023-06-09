package diploma.trackingApp.repositories;

import diploma.trackingApp.models.Interest;
import diploma.trackingApp.models.Student;
import diploma.trackingApp.models.Task;
import diploma.trackingApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findByTitle(String title);

    List<Task> findByTitleContainingAndInterests(String title, Interest importance);

    List<Task> findByTitleContaining(String title);

    List<Task> findByInterests(Interest importance);
}
