package com.Mamda.Mamda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Mamda.Mamda.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByUsername(String username);
    Admin findByEmail(String email);
    Admin findByResetToken(String resetToken);
}