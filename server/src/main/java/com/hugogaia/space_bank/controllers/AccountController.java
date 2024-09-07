package com.hugogaia.space_bank.controllers;

import com.hugogaia.space_bank.models.AccountModel;
import com.hugogaia.space_bank.repositories.AccountRepository;
import com.hugogaia.space_bank.services.AuthorizationService;
import com.hugogaia.space_bank.utils.TaxIdUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.UUID;

@RestController
public class AccountController {

    private final AuthorizationService authorizationService;
    private final AccountRepository accountRepository;

    public AccountController(AuthorizationService authorizationService, AccountRepository accountRepository) {
        this.authorizationService = authorizationService;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/account")
    public ResponseEntity<Map<String,Object>> detailAccount(HttpServletRequest request) {

        AccountModel account = authorizationService.authorize(request);

        String taxId = TaxIdUtils.hideTaxId(account.getTaxId());

        return ResponseEntity.ok(Map.of(
                "accountCode", account.getAccountCode(),
                "name", account.getName(),
                "email", account.getEmail(),
                "taxId", taxId,
                "balance", account.getBalance(),
                "birthDate", account.getBirthDate()
        ));
        
    }

    @GetMapping("/account/{accountCode}")
    public ResponseEntity<Map<String,Object>> detailAccountByCode(@PathVariable String accountCode, HttpServletRequest request) {

        AccountModel account = authorizationService.authorize(request);

        AccountModel desiredAccount = accountRepository.findByAccountCode(accountCode);

        if (desiredAccount == null) {
            return ResponseEntity.notFound().build();
        }

        String taxId = TaxIdUtils.hideTaxId(desiredAccount.getTaxId());

        return ResponseEntity.ok(Map.of(
                "accountCode", desiredAccount.getAccountCode(),
                "name", desiredAccount.getName(),
                "taxId", taxId
        ));

    }

}
