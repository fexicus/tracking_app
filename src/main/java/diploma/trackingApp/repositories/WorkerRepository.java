package diploma.trackingApp.repositories;

import diploma.trackingApp.models.Student;
import diploma.trackingApp.models.User;
import diploma.trackingApp.models.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Integer> {
    List<Worker> findByWorkUser(User user);

}