package com.example.dreamshop.data;

import com.example.dreamshop.model.Role;
import com.example.dreamshop.model.User;
import com.example.dreamshop.repository.RoleRepository;
import com.example.dreamshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;
private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_USER", "ROLE_ADMIN");
       // createDefaultUserIfNotExists();
        createDefaultRoleIfNotExists(defaultRoles);
         //createDefaultUserIfNotExists();

    }

    private void createDefaultUserIfNotExists() {

      Role  userRole =  roleRepository.findByName("ROLE_USER").get();
        for(int i = 1; i <= 5 ; i++){
            String defaultEmail = "user" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("User" + i);
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("User " + i + " created");

        }
    }

    private void createDefaultAdminIfNotExists() {
        Role  adminRole =  roleRepository.findByName("ROLE_ADMIN").get();
        for(int i = 1; i <= 2 ; i++){
            String defaultEmail = "admin" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456" ));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("Admin " + i + " created");

        }
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }

    private void createDefaultRoleIfNotExists(Set<String> roles) {
        roles.stream()
                .filter(role-> roleRepository.findByName(role).isEmpty())
                .map(Role::new).forEach(roleRepository::save);
    }
}
