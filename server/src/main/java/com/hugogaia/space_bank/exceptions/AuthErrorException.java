package com.hugogaia.space_bank.exceptions;

public class AuthErrorException extends RuntimeException {
    public AuthErrorException(String message) {
        super(message);
    }
}
