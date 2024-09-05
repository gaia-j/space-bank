package com.hugogaia.space_bank.controllers;


import com.hugogaia.space_bank.dtos.AuthDTO;
import com.hugogaia.space_bank.dtos.RegisterDTO;
import com.hugogaia.space_bank.infra.security.TokenService;
import com.hugogaia.space_bank.models.AccountModel;
import com.hugogaia.space_bank.repositories.AccountRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<Map<String,String>> login(@RequestBody @Valid AuthDTO data, HttpServletResponse response) {
        if(this.accountRepository.findByEmail(data.email()) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials."));
        }

        AccountModel account = this.accountRepository.findByEmail(data.email());

        if (!new BCryptPasswordEncoder().matches(data.password(), account.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials."));
        }

        String token = tokenService.GenerateToken(account.getEmail());

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);

        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);

        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message","sucess"));
    }

    @RequestMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterDTO data, HttpServletResponse response) {
        if (this.accountRepository.findByEmail(data.email()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already registered."));
        }
        if (this.accountRepository.findByTaxId(data.taxId()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Tax ID already registered."));
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

        String token = tokenService.GenerateToken(newAccount.getEmail());

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);

        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);

        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message","sucess"));
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
