package com.hugogaia.space_bank.services;

import com.hugogaia.space_bank.dtos.AuthDTO;
import com.hugogaia.space_bank.dtos.RegisterDTO;
import com.hugogaia.space_bank.exceptions.AccountAlreadyExistsException;
import com.hugogaia.space_bank.models.AccountModel;
import com.hugogaia.space_bank.repositories.AccountRepository;
import com.hugogaia.space_bank.utils.CookiesUtils;
import com.hugogaia.space_bank.utils.GenerateAccountCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

    private final AccountRepository accountRepository;
    private final TokenService tokenService;
    private final CookiesUtils cookiesUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AccountRepository accountRepository, TokenService tokenService,
            CookiesUtils cookiesUtils, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.tokenService = tokenService;
        this.cookiesUtils = cookiesUtils;
        this.passwordEncoder = passwordEncoder;
    }


    public void registerAccount(RegisterDTO data, HttpServletResponse response) throws AccountAlreadyExistsException {

        if (accountRepository.existsAccountModelByEmail(data.email())) {
            throw new AccountAlreadyExistsException("Email already registered.");
        }

        if (accountRepository.existsAccountModelByTaxId(data.taxId())) {
            throw new AccountAlreadyExistsException("Tax ID already registered.");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());

        String accountCode = GenerateAccountCode.generateAccountCode();
        while (accountRepository.existsAccountModelByAccountCode(accountCode)) {
            accountCode = GenerateAccountCode.generateAccountCode();
        }

        long randomNumber = generateRandomAccountNumber();

        AccountModel newAccount = new AccountModel(
                accountCode,
                data.name(),
                data.birthDate(),
                data.taxId(),
                data.email(),
                encryptedPassword,
                randomNumber
        );

        accountRepository.save(newAccount);

        String token = tokenService.GenerateToken(newAccount.getEmail());
        cookiesUtils.setTokenCookie(token, response);
    }

    public void Login(AuthDTO data, HttpServletResponse response) {
        if(this.accountRepository.findByEmail(data.email()) == null) {
            throw new AccountAlreadyExistsException("Invalid credentials.");
        }

        AccountModel account = this.accountRepository.findByEmail(data.email());

        if (!new BCryptPasswordEncoder().matches(data.password(), account.getPassword())) {
            throw new AccountAlreadyExistsException("Invalid credentials.");
        }

        String token = tokenService.GenerateToken(account.getEmail());

        cookiesUtils.setTokenCookie(token, response);
    }

    private long generateRandomAccountNumber() {
        Random random = new Random();
        long minValue = 1000000L;
        long maxValue = 4000000L;
        return minValue + (long)(random.nextDouble() * (maxValue - minValue + 1));
    }
}