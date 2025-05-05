package com.africahr.auth.config;

import com.africahr.auth.model.Role;
import com.africahr.auth.model.RoleName;
import com.africahr.auth.model.User;
import com.africahr.auth.repository.RoleRepository;
import com.africahr.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DataSeeder() {
        logger.info("DataSeeder initialized");
    }

    @Override
    public void run(String... args) {
        logger.info("Starting data seeding...");
        try {
            // Seed Roles if they don't exist
            seedRoles();
            
            // Seed Admin User if doesn't exist
            seedAdminUser();
            logger.info("Data seeding completed successfully.");
        } catch (Exception e) {
            logger.error("Error during data seeding: {}", e.getMessage(), e);
        }
    }

    private void seedRoles() {
        logger.info("Starting role seeding...");
        for (RoleName roleName : RoleName.values()) {
            try {
                if (roleRepository.findByName(roleName.name()).isEmpty()) {
                    Role role = new Role();
                    role.setName(roleName.name());
                    role.setDescription("Default role: " + roleName.name());
                    Role savedRole = roleRepository.save(role);
                    logger.info("Created role: {} with ID: {}", roleName.name(), savedRole.getId());
                } else {
                    logger.info("Role {} already exists", roleName.name());
                }
            } catch (Exception e) {
                logger.error("Error creating role {}: {}", roleName.name(), e.getMessage(), e);
            }
        }
        logger.info("Role seeding completed.");
    }

    private void seedAdminUser() {
        String adminEmail = "saddock2000@gmail.com";
        logger.info("Starting admin user seeding...");
        logger.info("Checking if admin user exists: {}", adminEmail);
        
        try {
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                logger.info("Creating admin user...");
                User admin = new User();
                admin.setEmail(adminEmail);
                String encodedPassword = passwordEncoder.encode("admin123");
                admin.setPassword(encodedPassword);
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setActive(true);

                Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN.name())
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
                
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                admin.setRoles(roles);

                User savedAdmin = userRepository.save(admin);
                logger.info("Admin user created successfully with ID: {}, email: {} and password: admin123", 
                    savedAdmin.getId(), adminEmail);
            } else {
                logger.info("Admin user already exists with email: {}", adminEmail);
            }
        } catch (Exception e) {
            logger.error("Error creating admin user: {}", e.getMessage(), e);
            throw e; // Re-throw to be caught by the main try-catch
        }
    }
} 