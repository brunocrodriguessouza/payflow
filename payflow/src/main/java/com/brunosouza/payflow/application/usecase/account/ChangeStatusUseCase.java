package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.AccountRepository;
import com.brunosouza.payflow.domain.account.Status;
import com.brunosouza.payflow.infraestructure.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

@Service
public class ChangeStatusUseCase {

    @Autowired
    private AccountRepository accountRepository;

    public AccountDTO handle(Long id, String status) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setStatus(status);
            account = accountRepository.save(account);
            return AccountMapper.convertToDTO(account);
        }else{
            throw new AccountNotFoundException("Account not found with id " + id);
        }
    }

}
