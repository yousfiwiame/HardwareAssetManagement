package com.Mamda.Mamda.config;

import com.Mamda.Mamda.model.Admin;
import com.Mamda.Mamda.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData(AdminRepository adminRepository){
        return args -> {
            if (adminRepository.findByEmail("wiame.yousfi22@gmail.com") == null) {
                Admin admin = new Admin();
                admin.setUsername("admin");
                String plainPassword = "adminmamda";
                String encodedPassword = passwordEncoder.encode(plainPassword);
                admin.setPassword(encodedPassword);
                admin.setEmail("wiame.yousfi22@gmail.com");
                adminRepository.save(admin);
            }
        };
    }

}
