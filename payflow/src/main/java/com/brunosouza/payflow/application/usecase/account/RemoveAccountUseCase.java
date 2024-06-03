package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.AccountRepository;
import com.brunosouza.payflow.infraestructure.AccountMapper;
import com.brunosouza.payflow.infraestructure.exception.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RemoveAccountUseCase {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    public void handle(Long id) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            accountRepository.deleteById(id);
        }else {
            throw new AccountNotFoundException("Account not found with id " + id);
        }
    }
}
