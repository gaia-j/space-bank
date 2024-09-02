package com.hugogaia.space_bank.repositories;

import com.hugogaia.space_bank.models.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountModel, Long> {
    AccountModel findByEmail(String email);
    AccountModel findByTaxId(String taxId);

}
