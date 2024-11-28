package com.example.identity_service.configuration;

import com.example.identity_service.entity.IdenUser;
import com.example.identity_service.entity.Permission;
import com.example.identity_service.entity.Role;
import com.example.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
           if (userRepository.findByUsername("admin").isEmpty() ){
               System.out.println("Starting");
               HashSet<Role> roles = new HashSet<>();
               HashSet<Permission> permissions = new HashSet<>();
               permissions.add(new Permission("LIST_ALL_DATA", "Admin can list all data"));
               Role adminRole = new Role(com.example.identity_service.enums.Role.ADMIN.name(), "ROLE_ADMIN", permissions);
               roles.add(adminRole);
               System.out.println("ROLE: " + roles.toString());
               IdenUser user = IdenUser.builder()
                       .username("admin")
                       .password(passwordEncoder.encode("admin"))
                       .roles(roles)
                       .build();
               userRepository.save(user);
               log.warn("Admin user has been created with default password: admin. Please change it!");
           }
        };
    }
}
