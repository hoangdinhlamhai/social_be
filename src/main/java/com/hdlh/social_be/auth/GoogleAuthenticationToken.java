package com.hdlh.social_be.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class GoogleAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public GoogleAuthenticationToken(Object principal) {
        super(principal, null); // Không cần credentials (ko cần password)
    }
}

