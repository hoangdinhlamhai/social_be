package com.hdlh.social_be.repository;

import com.hdlh.social_be.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);

    Account findByRefreshTokenAndEmail(String refreshToken, String email);
}
