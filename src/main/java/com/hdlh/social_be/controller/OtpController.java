package com.hdlh.social_be.controller;

import com.hdlh.social_be.service.EmailService;
import com.hdlh.social_be.service.OtpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtpController {
    private EmailService emailService;
    private OtpService otpService;

    public OtpController(EmailService emailService, OtpService otpService) {
        this.emailService = emailService;
        this.otpService = otpService;
    }

    @GetMapping("/send-otp")
    public String sendOtp(@RequestParam String email) {
        String otp = otpService.generateOtpAndSave(email);
        emailService.sendEmail(email, "Your OTP Code", "Your OTP is: " + otp);
        return "OTP sent successfully!";
    }
}
