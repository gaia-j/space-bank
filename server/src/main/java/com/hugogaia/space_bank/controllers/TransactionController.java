package com.hugogaia.space_bank.controllers;

import com.hugogaia.space_bank.dtos.TransactionDTO;
import com.hugogaia.space_bank.models.AccountModel;
import com.hugogaia.space_bank.services.TokenService;
import com.hugogaia.space_bank.services.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.hugogaia.space_bank.utils.ExceptionHandlers.handleValidationExceptionss;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final TokenService tokenService;

    @Autowired
    public TransactionController(TransactionService transactionService, TokenService tokenService) {
        this.transactionService = transactionService;
        this.tokenService = tokenService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Map<String, Object>>> listTransaction(HttpServletRequest request, HttpServletResponse response) {

        AccountModel account = tokenService.authorize(request);

        Long accountId = account.getId();

        List<Map<String, Object>> transactions = transactionService.getTransactionsForAccount(accountId);

        return ResponseEntity.ok(transactions);

    }

    @PostMapping("/send")
    public ResponseEntity<Map<String,String>> sendTransaction(@RequestBody @Valid TransactionDTO data, HttpServletRequest request) {

        try{
            AccountModel originAccount = tokenService.authorize(request);

            transactionService.sendTransaction(originAccount, data);

            return ResponseEntity.ok(Map.of("message", "Transaction sent"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return handleValidationExceptionss(ex);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> detailTransaction(@PathVariable UUID id, HttpServletRequest request, HttpServletResponse response) {

        try{
            AccountModel account = tokenService.authorize(request);

            var transaction = transactionService.detailTransaction(id, account);

            return ResponseEntity.ok(transaction);

        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }

    }
}
