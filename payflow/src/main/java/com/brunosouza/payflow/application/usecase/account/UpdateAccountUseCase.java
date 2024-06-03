package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.AccountRepository;
import com.brunosouza.payflow.infraestructure.AccountMapper;
import com.brunosouza.payflow.infraestructure.exception.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.brunosouza.payflow.infraestructure.AccountMapper.convertToDTO;
import static com.brunosouza.payflow.infraestructure.AccountMapper.convertToEntity;

@Service
public class UpdateAccountUseCase {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    public AccountDTO handle(Long id, AccountDTO accountDTO) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = convertToEntity(accountDTO);
            account.setId(id);
            account = accountRepository.save(account);
            return convertToDTO(account);
        }else{
            throw new AccountNotFoundException("Account not found with id " + id);
        }
    }
}
