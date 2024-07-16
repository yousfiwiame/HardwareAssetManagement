package com.Mamda.Mamda.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Mamda.Mamda.dto.AdminDto;
import com.Mamda.Mamda.model.PasswordResetToken;
import com.Mamda.Mamda.model.Admin;
import com.Mamda.Mamda.repository.AdminRepository;
import com.Mamda.Mamda.repository.TokenRepository;


@Service
public class AdminServiceImpl implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TokenRepository tokenRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            throw new UsernameNotFoundException("Invalid email or password.");
        }
        return new org.springframework.security.core.userdetails.User(admin.getEmail(), admin.getPassword(),
                new HashSet<GrantedAuthority>());
    }

    public Admin save(AdminDto adminDto) {
        Admin admin = new Admin();
        admin.setEmail(adminDto.getEmail());
        admin.setUsername(adminDto.getUsername());
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        return adminRepository.save(admin);
    }


    public String sendEmail(Admin admin) {
        try {
            String resetLink = generateResetToken(admin);

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("mysenderemailapp@gmail.com");// input the senders email ID
            msg.setTo(admin.getEmail());

            msg.setSubject("Welcome To My Channel");
            msg.setText("Hello \n\n" + "Please click on this link to Reset your Password :" + resetLink + ". \n\n"
                    + "Regards \n" + "ABC");

            javaMailSender.send(msg);

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

    }

    public String generateResetToken(Admin admin) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime expiryDateTime = currentDateTime.plusMinutes(15);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setAdmin(admin);
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDateTime(expiryDateTime);
        resetToken.setAdmin(admin);
        PasswordResetToken token = tokenRepository.save(resetToken);
        if (token != null) {
            String endpointUrl = "http://localhost:8080/resetPassword";
            return endpointUrl + "/" + resetToken.getToken();
        }
        return "";
    }

    public boolean hasExipred(LocalDateTime expiryDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return expiryDateTime.isAfter(currentDateTime);
    }

}
