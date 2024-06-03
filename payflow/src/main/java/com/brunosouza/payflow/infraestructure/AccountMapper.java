package com.brunosouza.payflow.infraestructure;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.Status;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public static Account convertToEntity(AccountDTO accountDTO) {
        return Account.builder()
                .dueDate(accountDTO.getDueDate())
                .paymentDate(accountDTO.getPaymentDate())
                .value(accountDTO.getValue())
                .description(accountDTO.getDescription())
                .status(accountDTO.getStatus().toString())
                .build();
    }

    public static AccountDTO convertToDTO(Account account) {
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
