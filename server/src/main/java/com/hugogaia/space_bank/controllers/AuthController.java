package com.hugogaia.space_bank.controllers;


import com.hugogaia.space_bank.dtos.AuthDTO;
import com.hugogaia.space_bank.dtos.RegisterDTO;
import com.hugogaia.space_bank.infra.security.TokenService;
import com.hugogaia.space_bank.models.AccountModel;
import com.hugogaia.space_bank.repositories.AccountRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("auth")
public class AuthController {


    private final AccountRepository accountRepository;
    private final TokenService tokenService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, AccountRepository accountRepository, TokenService tokenService) {
        this.accountRepository = accountRepository;
        this.tokenService = tokenService;
    }

    @RequestMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid AuthDTO data) {
        if(this.accountRepository.findByEmail(data.email()) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }

        AccountModel account = this.accountRepository.findByEmail(data.email());

        if (!new BCryptPasswordEncoder().matches(data.password(), account.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }

        String token = tokenService.GenerateToken(account.getEmail());

        return ResponseEntity.ok().body(token);
    }

    @RequestMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO data) {
        if (this.accountRepository.findByEmail(data.email()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered.");
        }
        if (this.accountRepository.findByTaxId(data.taxId()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CPF already registered.");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        Random random = new Random();
        long minValue = 1000000L;
        long maxValue = 4000000L;
        long randomNumber = minValue + (long)(random.nextDouble() * (maxValue - minValue + 1));

        AccountModel newAccount = new AccountModel(
                data.name(),
                data.birthDate(),
                data.taxId(),
                data.email(),
                encryptedPassword,
                randomNumber
        );

        accountRepository.save(newAccount);

        return ResponseEntity.ok().body("Account created successfully.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
