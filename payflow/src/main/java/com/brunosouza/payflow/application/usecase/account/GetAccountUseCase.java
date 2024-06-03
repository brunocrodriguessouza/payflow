package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.AccountRepository;
import com.brunosouza.payflow.infraestructure.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetAccountUseCase {

    @Autowired
    private AccountRepository accountRepository;


    public AccountDTO getAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.map(AccountMapper::convertToDTO).orElse(null);
    }

    public Page<AccountDTO> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable).map(AccountMapper::convertToDTO);
    }

}
