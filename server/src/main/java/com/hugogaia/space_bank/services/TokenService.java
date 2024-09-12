package com.hugogaia.space_bank.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.hugogaia.space_bank.models.AccountModel;
import com.hugogaia.space_bank.repositories.AccountRepository;
import com.hugogaia.space_bank.exceptions.UnauthorizedException;
import com.hugogaia.space_bank.utils.CookiesUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class TokenService {

    private final AccountRepository accountRepository;
    private final CookiesUtils cookiesUtils;

    @Value("${jwt.secret}")
    private String secret;

    public TokenService(AccountRepository accountRepository, CookiesUtils cookiesUtils) {
        this.accountRepository = accountRepository;
        this.cookiesUtils = cookiesUtils;
    }

    public AccountModel authorize(HttpServletRequest request) {

        String token = cookiesUtils.getTokenFromCookies(request);
        if (token == null) {
            throw new UnauthorizedException("Token is missing");
        }

        String email;

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            email = JWT.require(algorithm)
                    .withIssuer("Space Bank")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new UnauthorizedException("Invalid token");
        }

        AccountModel account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new UnauthorizedException("User not found");
        }
        return account;
    }

    public String GenerateToken(String email) {

        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("Space Bank")
                    .withSubject(email)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error creating token");
        }
    }

}
