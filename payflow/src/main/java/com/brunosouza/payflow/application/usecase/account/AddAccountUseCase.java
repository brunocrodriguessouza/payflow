package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.AccountRepository;
import com.brunosouza.payflow.infraestructure.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.brunosouza.payflow.infraestructure.AccountMapper.convertToDTO;
import static com.brunosouza.payflow.infraestructure.AccountMapper.convertToEntity;

@Service
public class AddAccountUseCase {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    public AccountDTO handle(AccountDTO accountDTO) {
        Account account = accountRepository.save(convertToEntity(accountDTO));
        return convertToDTO(account);
    }

}
