package com.brunosouza.payflow.domain.repository;

import com.brunosouza.payflow.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
