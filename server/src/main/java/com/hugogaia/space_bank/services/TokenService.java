package com.hugogaia.space_bank.services;

import com.hugogaia.space_bank.models.AccountModel;
import com.hugogaia.space_bank.repositories.AccountRepository;
import com.hugogaia.space_bank.exceptions.UnauthorizedException;
import com.hugogaia.space_bank.utils.CookiesUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TokenService {

    private final com.hugogaia.space_bank.infra.security.TokenService tokenService;
    private final AccountRepository accountRepository;
    private final CookiesUtils cookiesUtils;

    public TokenService(com.hugogaia.space_bank.infra.security.TokenService tokenService, AccountRepository accountRepository, CookiesUtils cookiesUtils) {
        this.tokenService = tokenService;
        this.accountRepository = accountRepository;
        this.cookiesUtils = cookiesUtils;
    }

    public AccountModel authorize(HttpServletRequest request) {
        String token = cookiesUtils.getTokenFromCookies(request);
        if (token == null) {
            throw new UnauthorizedException("Token is missing");
        }

        String email = tokenService.validateToken(token);
        if (Objects.equals(email, "invalid")) {
            throw new UnauthorizedException("Invalid token");
        }

        AccountModel account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new UnauthorizedException("User not found");
        }

        return account;
    }
}
