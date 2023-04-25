package diploma.trackingApp.services;

import diploma.trackingApp.models.Worker;
import diploma.trackingApp.repositories.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class WorkerService {

    private final WorkerRepository workerRepository;

    @Autowired
    public WorkerService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }
    public List<Worker> findAll(){
        return workerRepository.findAll();
    }

    public Worker findOne(int id){
        Optional<Worker> foundWorker = workerRepository.findById(id);
        return foundWorker.orElse(null);
    }
    @Transactional
    public void save(Worker worker){
        workerRepository.save(worker);
    }

    @Transactional
    public void update(int id, Worker updatedWorker){
        updatedWorker.setId(id);
        workerRepository.save(updatedWorker);
    }

    @Transactional
    public void delete(int id){
        workerRepository.deleteById(id);
    }
}
