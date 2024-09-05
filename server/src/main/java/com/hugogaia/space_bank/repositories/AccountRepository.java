package com.hugogaia.space_bank.repositories;

import com.hugogaia.space_bank.models.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountModel, Long> {


    Optional<AccountModel> findById(Long id);
    AccountModel findByEmail(String email);
    AccountModel findByTaxId(String taxId);

}
