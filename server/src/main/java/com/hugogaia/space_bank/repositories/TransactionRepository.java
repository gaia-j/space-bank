package com.hugogaia.space_bank.repositories;

import com.hugogaia.space_bank.models.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {
    TransactionModel findTransactionByExternalId(UUID externalId);
    TransactionModel findTransactionById(Long id);
    List<TransactionModel> findTransactionByOriginAccount(Long originAccount);
    List<TransactionModel> findTransactionByDestinationAccount(Long destinationAccount);
}
