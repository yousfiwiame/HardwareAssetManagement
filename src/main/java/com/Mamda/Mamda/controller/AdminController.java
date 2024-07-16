package com.Mamda.Mamda.controller;

import com.Mamda.Mamda.dto.AdminDto;
import com.Mamda.Mamda.dto.AdminLoginDto;
import com.Mamda.Mamda.model.Admin;
import com.Mamda.Mamda.model.PasswordResetToken;
import com.Mamda.Mamda.repository.AdminRepository;
import com.Mamda.Mamda.repository.TokenRepository;
import com.Mamda.Mamda.service.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class AdminController {

    @Autowired
    AdminServiceImpl adminService;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    TokenRepository tokenRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @GetMapping("/login")
    public String showLoginForm(){
        return "login";
    }

    @PostMapping("/login")
    public void login(@ModelAttribute AdminLoginDto adminLoginDto, Model model){
        adminService.loadUserByUsername(adminLoginDto.getEmail());
    }

    @GetMapping("/adminDashboard")
    public String showAdminDashboard(){
        return "adminDashboard";
    }

    @GetMapping("/forgotPassword")
    public String showForgotPasswordPage(){
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPasswordProcess(@ModelAttribute AdminDto adminDto){
        String output ="";
        Admin admin = adminRepository.findByEmail(adminDto.getEmail());
        if(admin != null){
            output = adminService.sendEmail(admin);
        }
        if (output.equals("success")) {
            return "redirect:/forgotPassword?success";
        }
        return "redirect:/login?error";
    }

    @GetMapping("/resetPassword/{token}")
    public String resetPasswordForm(@PathVariable String token, Model model) {
        PasswordResetToken reset = tokenRepository.findByToken(token);
        if (reset != null && adminService.hasExipred(reset.getExpiryDateTime())) {
            model.addAttribute("email", reset.getAdmin().getEmail());
            return "resetPassword";
        }
        return "redirect:/forgotPassword?error";
    }

    @PostMapping("/resetPassword")
    public String passwordResetProcess(@ModelAttribute AdminDto adminDto) {
        Admin admin = adminRepository.findByEmail(adminDto.getEmail());
        if(admin!= null) {
            admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
            adminRepository.save(admin);
        }
        return "redirect:/login";
    }

}
