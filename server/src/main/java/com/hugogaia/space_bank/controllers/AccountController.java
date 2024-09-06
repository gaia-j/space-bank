package com.hugogaia.space_bank.controllers;

import com.hugogaia.space_bank.models.AccountModel;
import com.hugogaia.space_bank.services.AuthorizationService;
import com.hugogaia.space_bank.utils.TaxIdUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class AccountController {

    private final AuthorizationService authorizationService;

    public AccountController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
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
    public ResponseEntity<Map<String,Object>> detailAccountByCode(HttpServletRequest request) {

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

}
