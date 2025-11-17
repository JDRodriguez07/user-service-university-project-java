package edu.university.user_service.config;

import edu.university.user_service.enums.DocumentType;
import edu.university.user_service.enums.UserStatus;
import edu.university.user_service.model.Administrator;
import edu.university.user_service.model.Role;
import edu.university.user_service.repository.AdministratorRepository;
import edu.university.user_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AdministratorRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // ==========================================================
        // 1. Crear roles si no existen
        // ==========================================================

        // ADMIN
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ADMIN");
                    r.setDescription("Administrator role");
                    return roleRepository.save(r);
                });

        // TEACHER
        Role teacherRole = roleRepository.findByName("TEACHER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("TEACHER");
                    r.setDescription("Teacher role");
                    return roleRepository.save(r);
                });

        // STUDENT
        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("STUDENT");
                    r.setDescription("Student role");
                    return roleRepository.save(r);
                });

        // ==========================================================
        // 2. Crear admin por defecto si no existe
        // ==========================================================

        if (!adminRepository.existsByEmail("admin@system.com")) {

            Administrator admin = new Administrator();
            admin.setEmail("admin@system.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(adminRole);
            admin.setStatus(UserStatus.ACTIVE);

            // Datos heredados de PERSON
            admin.setDocumentType(DocumentType.CC);
            admin.setDni("0000000000");
            admin.setName("SYSTEM");
            admin.setLastName("ADMIN");
            admin.setGender("N/A");
            admin.setBirthDate(null);
            admin.setPhoneNumber("0000000000");
            admin.setAddress("SYSTEM_ADDRESS");

            // Datos propios de ADMIN
            admin.setAdminCode("ADM-SYSTEM");
            admin.setDepartment("SYSTEM");
            admin.setPosition("SYSTEM ADMINISTRATOR");

            adminRepository.save(admin);

            System.out.println("\n==============================");
            System.out.println(" DEFAULT ADMIN CREATED:");
            System.out.println(" email: admin@system.com");
            System.out.println(" password: admin123");
            System.out.println("==============================\n");
        }
    }
}