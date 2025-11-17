package edu.university.user_service.config;

import edu.university.user_service.enums.DocumentType;
import edu.university.user_service.enums.UserStatus;
import edu.university.user_service.model.Administrator;
import edu.university.user_service.model.Role;
import edu.university.user_service.repository.AdministratorRepository;
import edu.university.user_service.repository.RoleRepository;
import edu.university.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Crear rol ADMIN si no existe
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ADMIN");
                    r.setDescription("System administrator role");
                    return roleRepository.save(r);
                });

        // 2. Crear administrador inicial por defecto si no existe

        String adminEmail = "admin@system.com";

        // Verificamos en la tabla users para no duplicar email
        if (!userRepository.existsByEmail(adminEmail)) {

            Administrator admin = new Administrator();

            // --------- Campos heredados de User ---------
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("Admin123"));
            admin.setStatus(UserStatus.ACTIVE);
            admin.setRole(adminRole);

            // --------- Campos heredados de Person ---------
            admin.setDocumentType(DocumentType.CC); // CC / TI / CE / PAS
            admin.setDni("9999999999"); // algo fijo para el sistema
            admin.setName("SYSTEM");
            admin.setLastName("ADMIN");
            admin.setGender("N/A"); // o "OTRO"
            admin.setBirthDate(LocalDate.of(2000, 1, 1)); // fecha dummy
            admin.setPhoneNumber("0000000000");
            admin.setAddress("SYSTEM INTERNAL ADDRESS");

            // --------- Campos propios de Administrator ---------
            admin.setAdminCode("ADM000000"); // código fijo para el admin del sistema
            admin.setDepartment("SYSTEM");
            admin.setPosition("SUPER_ADMIN");

            administratorRepository.save(admin);

            System.out.println("\n========================================");
            System.out.println(" DEFAULT SYSTEM ADMINISTRATOR CREATED");
            System.out.println("  EMAIL   : " + adminEmail);
            System.out.println("  PASSWORD: admin123");
            System.out.println("  ROLE    : ADMIN (Administrator entity)");
            System.out.println("========================================\n");
        }
    }
}
