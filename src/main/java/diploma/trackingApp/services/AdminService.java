package diploma.trackingApp.services;

import diploma.trackingApp.models.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*@Service
@Transactional
public class AdminService {
*//*    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    public Admin findOne(int id){
        Optional<Admin> foundAdmin = adminRepository.findById(id);
        return foundAdmin.orElse(null);
    }*//*
}*/
