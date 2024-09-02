package com.hugogaia.space_bank.dtos;

public record AuthDTO(String email, String password) {
    // Validate email and password
    public boolean isValid() {
        return email != null && email.contains("@") && password != null && password.length() >= 6;
    }
}
