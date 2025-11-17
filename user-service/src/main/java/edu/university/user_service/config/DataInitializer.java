package edu.university.user_service.config;

import edu.university.user_service.enums.UserStatus;
import edu.university.user_service.model.Role;
import edu.university.user_service.model.User;
import edu.university.user_service.repository.RoleRepository;
import edu.university.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Crear rol ADMIN si no existe
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ADMIN");
                    return roleRepository.save(r);
                });

        // Crear usuario ADMIN inicial
        if (!userRepository.existsByEmail("admin@system.com")) {

            User admin = new User();
            admin.setEmail("admin@system.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(adminRole);
            admin.setStatus(UserStatus.ACTIVE);

            userRepository.save(admin);

            System.out.println("\n==============================");
            System.out.println(" ADMIN USER CREATED:");
            System.out.println(" email: admin@system.com");
            System.out.println(" password: admin123");
            System.out.println("==============================\n");
        }
    }
}
