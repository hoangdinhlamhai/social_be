package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Account;
import com.hdlh.social_be.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpService {
    private final SecureRandom secureRandom = new SecureRandom();
    private final AccountRepository accountRepository;

    public OtpService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String generateOtpAndSave(String email) {
        int otp = 100000 + secureRandom.nextInt(900000);

        Account account = accountRepository.findByEmail(email);

        if (account != null) {
            account.setOtp(String.valueOf(otp));
            account.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        }

        this.accountRepository.save(account);

        return String.valueOf(otp);
    }
}
