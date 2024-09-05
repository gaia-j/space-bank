package com.hugogaia.space_bank.utils;

public class TaxIdUtils {
    public static String hideTaxId(String taxId) {
        return "***" + taxId.substring(3, 9) + "**";
    }
}
