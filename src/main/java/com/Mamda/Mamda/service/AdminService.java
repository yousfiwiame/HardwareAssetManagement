package com.Mamda.Mamda.service;

import com.Mamda.Mamda.model.Admin;
import com.Mamda.Mamda.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
public class AdminService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            throw new UsernameNotFoundException("Administrator not found");
        }
        return new User(admin.getEmail(), admin.getPassword(), Collections.singleton(() -> "ROLE_ADMIN"));
    }


    public void sendResetPasswordEmail(String email) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            throw new UsernameNotFoundException("Administrator not found");
        }
        String token = UUID.randomUUID().toString();
        admin.setResetToken(token);
        admin.setResetTokenExpiryDate(LocalDateTime.now().plusMinutes(15));
        adminRepository.save(admin);

        String resetLink = "http://localhost:8080/resetPassword?token=" + token;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("mysenderemailapp@gmail.com");
        mailMessage.setTo(admin.getEmail());
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("To reset your password, click the link below:\n" + resetLink);
        mailSender.send(mailMessage);
    }

    public void resetPassword(String token, String newPassword) {
        Admin admin = adminRepository.findByResetToken(token);
        if (admin == null) {
            throw new IllegalArgumentException("Invalid token");
        }
        if (admin.getResetTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }
        admin.setPassword(passwordEncoder.encode(newPassword));
        admin.setResetToken(null);
        admin.setResetTokenExpiryDate(null);
        adminRepository.save(admin);
    }

}
