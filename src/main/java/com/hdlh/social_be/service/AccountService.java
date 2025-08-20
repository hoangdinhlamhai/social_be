package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Account;
import com.hdlh.social_be.repository.AccountRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
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

    //register
    public boolean checkIfAccountExists(String email) {
        return accountRepository.existsByEmail(email);
    }

    public Account handleSaveAccount(Account account) {
        return this.accountRepository.save(account);
    }

    public Account handleChangePassword(String newPassword, String email) {
        Account currentAccount = this.accountRepository.findByEmail(email);
        if (currentAccount != null) {
            String newPasswordHash = passwordEncoder.encode(newPassword);
            currentAccount.setPassword(newPasswordHash);
            return this.accountRepository.save(currentAccount);
        }
        throw new UsernameNotFoundException("Không tìm thấy tài khoản với email: " + email);
    }
}
