package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Account;
import com.hdlh.social_be.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account handleCreateAccount(Account account) {
        return accountRepository.save(account);
    }
}
