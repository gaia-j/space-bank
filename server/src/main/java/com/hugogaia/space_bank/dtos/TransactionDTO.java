package com.hugogaia.space_bank.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TransactionDTO(
        @NotBlank(message = "Destination tax ID is required")
        String destinationTaxId,

        @NotNull(message = "Amount is required")
        Long amount
) {
}
