package diploma.trackingApp.repositories;

import diploma.trackingApp.models.Student;
import diploma.trackingApp.models.User;
import diploma.trackingApp.models.Worker;
import org.hibernate.jdbc.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Integer> {

    List<Worker> getWorkerByDepartment(String department);
    @Query("SELECT DISTINCT w.department FROM Worker w")
    List<String> findAllDepartment();

    List<Worker> findByDepartment(String department);
}