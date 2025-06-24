package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Account;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component("userDetailsService") // ghi đè lại bean => quy tắc đặt tên là tự động đổi chữ hoa đầu tiên thành chữ thường
public class UserDetailsCustom implements UserDetailsService {
    private final AccountService accountService;

    public UserDetailsCustom(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = this.accountService.handleGetAccountByUsername(email);
        if (account == null) {
            throw new UsernameNotFoundException("Email/password khong duoc de trong");
        }
        return new org.springframework.security.core.userdetails.User(
                account.getEmail(),
                account.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

    }
}
