package com.hugogaia.space_bank.controllers;


import com.hugogaia.space_bank.dtos.AuthDTO;
import com.hugogaia.space_bank.dtos.RegisterDTO;
import com.hugogaia.space_bank.infra.security.TokenService;
import com.hugogaia.space_bank.models.AccountModel;
import com.hugogaia.space_bank.repositories.AccountRepository;
import com.hugogaia.space_bank.utils.CookiesUtils;
import com.hugogaia.space_bank.utils.GenerateAccountCode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

import static com.hugogaia.space_bank.utils.ExceptionHandlers.handleValidationExceptionss;

@RestController
@RequestMapping("auth")
public class AuthController {


    private final AccountRepository accountRepository;
    private final TokenService tokenService;
    private final CookiesUtils cookiesUtils;

    @Autowired
    public AuthController(
            AccountRepository accountRepository,
            TokenService tokenService,
            CookiesUtils cookiesUtils
    ){
        this.accountRepository = accountRepository;
        this.tokenService = tokenService;
        this.cookiesUtils = cookiesUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody @Valid AuthDTO data, HttpServletResponse response) {
        if(this.accountRepository.findByEmail(data.email()) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials."));
        }

        AccountModel account = this.accountRepository.findByEmail(data.email());

        if (!new BCryptPasswordEncoder().matches(data.password(), account.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials."));
        }

        String token = tokenService.GenerateToken(account.getEmail());

        cookiesUtils.setTokenCookie(token, response);

        return ResponseEntity.ok(Map.of("message","sucess"));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterDTO data, HttpServletResponse response) {
        if (this.accountRepository.existsAccountModelByEmail(data.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already registered."));
        }
        if (this.accountRepository.existsAccountModelByTaxId(data.taxId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Tax ID already registered."));
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        Random random = new Random();
        long minValue = 1000000L;
        long maxValue = 4000000L;
        long randomNumber = minValue + (long)(random.nextDouble() * (maxValue - minValue + 1));

        String accountCode = GenerateAccountCode.generateAccountCode();

        Boolean accountCodeExists = accountRepository.existsAccountModelByAccountCode(accountCode);

        while (accountCodeExists) {
            accountCode = GenerateAccountCode.generateAccountCode();
            accountCodeExists = accountRepository.existsAccountModelByAccountCode(accountCode);
        }

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

        return ResponseEntity.ok(Map.of("message","sucess"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return handleValidationExceptionss(ex);
    }

}
