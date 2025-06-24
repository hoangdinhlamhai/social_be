package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Account;
import com.hdlh.social_be.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account handleCreateAccount(Account account) {
        return accountRepository.save(account);
    }

    public void updateRefreshTokenAccount(String refreshToken, String email) {
        Account account = this.accountRepository.findByEmail(email);
        if (account != null) {
            account.setRefreshToken(refreshToken);
            this.accountRepository.save(account);
        }
    }

    public Account handleGetAccountByUsername(String email) {
        return this.accountRepository.findByEmail(email);
    }

    //api logout
    public void clearRefreshToken(String email) {
        Optional<Account> userOptional = Optional.ofNullable(accountRepository.findByEmail(email));
        userOptional.ifPresent(user -> {
            user.setRefreshToken(null);
            accountRepository.save(user);
        });
    }

    //api refresh token
    public Account getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        return this.accountRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }
}
