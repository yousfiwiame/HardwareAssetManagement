package com.Mamda.Mamda.repository;

import com.Mamda.Mamda.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<PasswordResetToken, Integer> {

    PasswordResetToken findByToken(String token);
}
