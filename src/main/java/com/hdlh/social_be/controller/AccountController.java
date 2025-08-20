package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.Account;
import com.hdlh.social_be.domain.dto.ChangePasswordRequest;
import com.hdlh.social_be.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    public AccountController(PasswordEncoder passwordEncoder, AccountService accountService) {
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        //hash password
        String hashedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(hashedPassword);
        return ResponseEntity.ok(this.accountService.handleCreateAccount(account));
    }

    //getmapping ko choi @RequestBody
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(this.accountService.checkIfAccountExists(email));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Account> changePassword(@RequestBody ChangePasswordRequest request, @RequestParam String email) {
        return ResponseEntity.ok(this.accountService.handleChangePassword(request.getPassword(), email));
    }
}
