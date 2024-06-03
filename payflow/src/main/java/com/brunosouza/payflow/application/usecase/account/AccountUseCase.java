package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.AccountRepository;
import com.brunosouza.payflow.domain.account.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountUseCase {

    @Autowired
    private AccountRepository accountRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    public AccountDTO getAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.map(this::toDTO).orElse(null);
    }

    public Page<AccountDTO> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable).map(this::toDTO);
    }

    public BigDecimal findTotalValuePaidByPeriod(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDt = LocalDate.parse(startDate, formatter);
        LocalDate endDt = LocalDate.parse(endDate, formatter);
        return accountRepository.findTotalValuePaidByPeriod(startDt, endDt);
    }

    public Page<AccountDTO> findByDueDateAndDescription(String dueDate, String description, Pageable pageable) {

        boolean dueDateProvided = !Objects.isNull(dueDate) && !dueDate.isEmpty();
        boolean descriptionProvided = !Objects.isNull(description) && !description.isEmpty();

        if (dueDateProvided && descriptionProvided) {
            LocalDate parsedDueDate = LocalDate.parse(dueDate, formatter);
            return accountRepository.findByDueDateAndDescriptionContaining(parsedDueDate, description, pageable)
                    .map(this::toDTO);
        }

        if (descriptionProvided) {
            return accountRepository.findByDescriptionContaining(description, pageable)
                    .map(this::toDTO);
        }

        if (dueDateProvided) {
            LocalDate parsedDueDate = LocalDate.parse(dueDate, formatter);
            return accountRepository.findByDueDate(parsedDueDate, pageable)
                    .map(this::toDTO);
        }

        return accountRepository.findAll(pageable).map(this::toDTO);
    }

    private AccountDTO toDTO(Account account) {
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
