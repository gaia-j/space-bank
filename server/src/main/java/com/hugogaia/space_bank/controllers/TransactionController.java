package com.hugogaia.space_bank.controllers;


import com.hugogaia.space_bank.dtos.TransactionDTO;
import com.hugogaia.space_bank.infra.security.TokenService;
import com.hugogaia.space_bank.models.AccountModel;
import com.hugogaia.space_bank.models.TransactionModel;
import com.hugogaia.space_bank.repositories.AccountRepository;
import com.hugogaia.space_bank.repositories.TransactionRepository;
import com.hugogaia.space_bank.utils.CookiesUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

import static com.hugogaia.space_bank.utils.ExceptionHandlers.handleValidationExceptionss;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CookiesUtils cookiesUtils;
    private final TokenService tokenService;

    @Autowired
    public TransactionController(
            TransactionRepository transactionRepository,
            CookiesUtils cookiesUtils,
            TokenService tokenService,
            AccountRepository accountRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.cookiesUtils = cookiesUtils;
        this.tokenService = tokenService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Map<String, Object>>> listTransaction(HttpServletRequest request, HttpServletResponse response) {

        String token = cookiesUtils.getTokenFromCookies(request);

        if (token == null) {
            return ResponseEntity.status(401).build();
        }

        String email = tokenService.validateToken(token);

        if (Objects.equals(email, "invalid")) {
            return ResponseEntity.status(401).build();
        }

        AccountModel account = accountRepository.findByEmail(email);

        if (account == null) {
            return ResponseEntity.status(400).build();
        }

        Long accountId = account.getId();

        List<Map<String, Object>> transactions = new ArrayList<>();

        List<TransactionModel> sentTransactions = transactionRepository.findTransactionByOriginAccount(accountId);
        List<TransactionModel> receivedTransactions = transactionRepository.findTransactionByDestinationAccount(accountId);

        for (TransactionModel transaction : sentTransactions) {
            Optional<AccountModel> destinationAccount = accountRepository.findById(transaction.getDestinationAccount());
            destinationAccount.ifPresent(accountModel -> transactions.add(Map.of(
                    "amount", transaction.getAmount(),
                    "time", transaction.getCreatedAt(),
                    "id", transaction.getExternalId(),
                    "name", accountModel.getName(),
                    "type", "sent"
            )));
        }

        for (TransactionModel transaction : receivedTransactions) {
            Optional<AccountModel> originAccount = accountRepository.findById(transaction.getOriginAccount());
            originAccount.ifPresent(accountModel -> transactions.add(Map.of(
                    "amount", transaction.getAmount(),
                    "time", transaction.getCreatedAt(),
                    "id", transaction.getExternalId(),
                    "name", accountModel.getName(),
                    "type", "received"
            )));
        }

        transactions.sort((a, b) -> {
            LocalDateTime dateA = (LocalDateTime) a.get("time");
            LocalDateTime dateB = (LocalDateTime) b.get("time");
            return dateB.compareTo(dateA);
        });

        return ResponseEntity.ok(transactions);

    }

    @PostMapping("/send")
    public ResponseEntity<Map<String,String>> sendTransaction(@RequestBody @Valid TransactionDTO data, HttpServletRequest request, HttpServletResponse response) {

        String token = cookiesUtils.getTokenFromCookies(request);

        if (token == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        String originEmail = tokenService.validateToken(token);

        if (Objects.equals(originEmail, "invalid")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        AccountModel originAccount = accountRepository.findByEmail(originEmail);
        AccountModel destinationAccount = accountRepository.findByTaxId(data.destinationTaxId());

        if (originAccount == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Account not found"));
        }

        if (destinationAccount == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Destination account not found"));
        }

        if (originAccount.getTaxId().equals(destinationAccount.getTaxId())) {
            return ResponseEntity.status(400).body(Map.of("error", "You can't send money to yourself"));
        }

        if (originAccount.getBalance() < data.amount()) {
            return ResponseEntity.status(400).body(Map.of("error", "Insufficient funds"));
        }


        UUID uuid = UUID.randomUUID();

        TransactionModel transaction = new TransactionModel(
                uuid,
                data.amount(),
                originAccount.getId(),
                destinationAccount.getId()
        );


        originAccount.setBalance(originAccount.getBalance() - data.amount());
        destinationAccount.setBalance(destinationAccount.getBalance() + data.amount());


        transactionRepository.save(transaction);
        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);

        return ResponseEntity.ok(Map.of("message", "Transaction sent"));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return handleValidationExceptionss(ex);
    }


}
