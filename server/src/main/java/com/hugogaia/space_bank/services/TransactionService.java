package com.hugogaia.space_bank.services;

import com.hugogaia.space_bank.dtos.TransactionDTO;
import com.hugogaia.space_bank.models.AccountModel;
import com.hugogaia.space_bank.models.TransactionModel;
import com.hugogaia.space_bank.repositories.AccountRepository;
import com.hugogaia.space_bank.repositories.TransactionRepository;
import com.hugogaia.space_bank.utils.TaxIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public List<Map<String, Object>> getTransactionsForAccount(Long accountId) {

        List<Map<String, Object>> transactions = new ArrayList<>();

        List<TransactionModel> sentTransactions = transactionRepository.findTransactionByOriginAccount(accountId);
        List<TransactionModel> receivedTransactions = transactionRepository.findTransactionByDestinationAccount(accountId);

        for (TransactionModel transaction : sentTransactions) {
            Optional<AccountModel> destinationAccount = accountRepository.findById(transaction.getDestinationAccount());
            destinationAccount.ifPresent(accountModel -> transactions.add(Map.of(
                    "amount", transaction.getAmount(),
                    "time", transaction.getCreatedAt(),
                    "id", transaction.getExternalId(),
                    "name", accountModel.getName(),
                    "type", "sent"
            )));
        }

        for (TransactionModel transaction : receivedTransactions) {
            Optional<AccountModel> originAccount = accountRepository.findById(transaction.getOriginAccount());
            originAccount.ifPresent(accountModel -> transactions.add(Map.of(
                    "amount", transaction.getAmount(),
                    "time", transaction.getCreatedAt(),
                    "id", transaction.getExternalId(),
                    "name", accountModel.getName(),
                    "type", "received"
            )));
        }

        transactions.sort((a, b) -> {
            LocalDateTime dateA = (LocalDateTime) a.get("time");
            LocalDateTime dateB = (LocalDateTime) b.get("time");
            return dateB.compareTo(dateA);
        });

        return transactions;
    }

    public void sendTransaction(AccountModel originAccount, TransactionDTO data) throws Exception {
        AccountModel destinationAccount = accountRepository.findByAccountCode(data.destinationAccountCode());

        if (destinationAccount == null) {
            throw new Exception("Destination account not found");
        }

        if (originAccount.getTaxId().equals(destinationAccount.getTaxId())) {
            throw new Exception("You can't send money to yourself");
        }

        if (originAccount.getBalance() < data.amount()) {
            throw new Exception("Insufficient funds");
        }


        UUID uuid = UUID.randomUUID();

        TransactionModel transaction = new TransactionModel(
                uuid,
                data.amount(),
                originAccount.getId(),
                destinationAccount.getId()
        );


        originAccount.setBalance(originAccount.getBalance() - data.amount());
        destinationAccount.setBalance(destinationAccount.getBalance() + data.amount());


        transactionRepository.save(transaction);
        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);
    }

    public Map <String, Object> detailTransaction(UUID transactionId, AccountModel account) throws Exception {

        TransactionModel transaction = transactionRepository.findTransactionByExternalId(transactionId);

        Long accountId = account.getId();

        if (transaction == null) {
            throw new Exception("Transaction not found");
        }

        if (!Objects.equals(transaction.getOriginAccount(), accountId) && !Objects.equals(transaction.getDestinationAccount(), accountId)) {
            throw new Exception("Transaction not found");
        }

        Optional<AccountModel> originAccount = accountRepository.findById(transaction.getOriginAccount());
        Optional<AccountModel> destinationAccount = accountRepository.findById(transaction.getDestinationAccount());

        String originTaxId = Objects.requireNonNull(originAccount.map(AccountModel::getTaxId).orElse(null));
        String destinationTaxId = Objects.requireNonNull(destinationAccount.map(AccountModel::getTaxId).orElse(null));

        originTaxId = TaxIdUtils.hideTaxId(originTaxId);
        destinationTaxId = TaxIdUtils.hideTaxId(destinationTaxId);

        return Map.of(
                "amount", transaction.getAmount(),
                "time", transaction.getCreatedAt(),
                "id", transaction.getExternalId(),
                "origin", Objects.requireNonNull(originAccount.map(AccountModel::getName).orElse(null)),
                "originTaxId", originTaxId,
                "destination", Objects.requireNonNull(destinationAccount.map(AccountModel::getName).orElse(null)),
                "detinationTaxId", destinationTaxId
        );
    }

}
