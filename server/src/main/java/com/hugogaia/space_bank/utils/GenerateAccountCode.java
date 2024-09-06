package com.hugogaia.space_bank.utils;

public class GenerateAccountCode {
    public static String generateAccountCode() {
        StringBuilder accountCode = new StringBuilder();
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        for (int i = 0; i < 1; i++) {
            accountCode.append(letters.charAt((int) (Math.random() * letters.length())));
        }
        for (int i = 0; i < 1; i++) {
            accountCode.append(numbers.charAt((int) (Math.random() * numbers.length())));
        }
        for (int i = 0; i < 3; i++) {
            accountCode.append(letters.charAt((int) (Math.random() * letters.length())));
        }
        for (int i = 0; i < 3; i++) {
            accountCode.append(numbers.charAt((int) (Math.random() * numbers.length())));
        }
        return accountCode.toString();
    }
}
