package com.brunosouza.payflow.application.dto;

import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {
    private Long id;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private BigDecimal value;
    private String description;
    private Status status;

    public static AccountDTO toDTO(Account account) {
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
