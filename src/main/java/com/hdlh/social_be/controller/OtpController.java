package com.hdlh.social_be.controller;

import com.hdlh.social_be.service.EmailService;
import com.hdlh.social_be.service.OtpService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        String otp = otpService.generateOtpAndSave(email);
        emailService.sendEmail(email, "Your OTP Code", "Your OTP is: " + otp);
        return ResponseEntity.ok("Send OTP successful");
    }

    @GetMapping("/verify-otp")
    public ResponseEntity<Boolean> verifyOtp(@RequestParam String otp, String email) {
        return ResponseEntity.ok(this.otpService.validateOtp(otp, email));
    }
}
