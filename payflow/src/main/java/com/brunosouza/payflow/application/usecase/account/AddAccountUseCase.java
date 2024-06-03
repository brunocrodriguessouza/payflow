package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.AccountRepository;
import com.brunosouza.payflow.domain.account.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddAccountUseCase {

    @Autowired
    private AccountRepository accountRepository;

    public AccountDTO handle(AccountDTO accountDTO) {
        Account account = accountRepository.save(convertToEntity(accountDTO));
        return convertToDTO(account);
    }

    private Account convertToEntity(AccountDTO accountDTO) {
        return Account.builder()
                .dueDate(accountDTO.getDueDate())
                .paymentDate(accountDTO.getPaymentDate())
                .value(accountDTO.getValue())
                .description(accountDTO.getDescription())
                .status(accountDTO.getStatus().toString())
                .build();
    }

    private AccountDTO convertToDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .dueDate(account.getDueDate())
                .paymentDate(account.getPaymentDate())
                .value(account.getValue())
                .description(account.getDescription())
                .status(Status.valueOf(account.getStatus()))
                .build();
    }
}
