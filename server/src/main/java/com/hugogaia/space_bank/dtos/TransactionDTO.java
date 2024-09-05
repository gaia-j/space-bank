package com.hugogaia.space_bank.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TransactionDTO(
        @NotBlank(message = "Destination account code is required")
        String destinationAccountCode,

        @NotNull(message = "Amount is required")
        Long amount
) {
}
