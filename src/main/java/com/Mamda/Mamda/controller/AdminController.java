package com.Mamda.Mamda.controller;

import com.Mamda.Mamda.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "resetSuccess", required = false) String resetSuccess, Model model) {
        if (resetSuccess != null) {
            model.addAttribute("successMessage", "Password updated successfully. Please log in.");
        }
        return "login";
    }

    @GetMapping("/adminDashboard")
    public String adminDashboard(){
        return "adminDashboard";
    }

    @GetMapping("/forgotPassword")
    public String showForgotPasswordPage(){
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPasswordForm(@RequestParam("email") String email, Model model) {
        try {
            adminService.sendResetPasswordEmail(email);
            model.addAttribute("successMessage", "A password reset link has been sent to " + email);
        } catch (UsernameNotFoundException e) {
            model.addAttribute("errorMessage", "The email address is not valid.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while sending the reset email.");
        }
        return "forgotPassword";
    }

    @GetMapping("/resetPassword")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String processResetPasswordForm(@RequestParam("token") String token,
                                           @RequestParam("newPassword") String newPassword,
                                           @RequestParam("confirmPassword") String confirmPassword,
                                           Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match.");
            model.addAttribute("token", token);
            return "resetPassword";
        }
        try {
            adminService.resetPassword(token, newPassword);
            return "redirect:/login?resetSuccess";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("token", token);
            return "resetPassword";
        }
    }

}
