package com.hugogaia.space_bank.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @PostMapping("/account")
    public String createAccount() {
        return "Account created";
    }

}
