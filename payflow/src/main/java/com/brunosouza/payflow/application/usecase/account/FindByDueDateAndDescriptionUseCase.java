package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.domain.account.AccountRepository;
import com.brunosouza.payflow.infraestructure.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class FindByDueDateAndDescriptionUseCase {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Page<AccountDTO> handle(String dueDate, String description, Pageable pageable) {

        boolean dueDateProvided = !Objects.isNull(dueDate) && !dueDate.isEmpty();
        boolean descriptionProvided = !Objects.isNull(description) && !description.isEmpty();

        if (dueDateProvided && descriptionProvided) {
            LocalDate parsedDueDate = LocalDate.parse(dueDate, formatter);
            return accountRepository.findByDueDateAndDescriptionContaining(parsedDueDate, description, pageable)
                    .map(AccountMapper::convertToDTO);
        }

        if (descriptionProvided) {
            return accountRepository.findByDescriptionContaining(description, pageable)
                    .map(AccountMapper::convertToDTO);
        }

        if (dueDateProvided) {
            LocalDate parsedDueDate = LocalDate.parse(dueDate, formatter);
            return accountRepository.findByDueDate(parsedDueDate, pageable)
                    .map(AccountMapper::convertToDTO);
        }

        return accountRepository.findAll(pageable).map(AccountMapper::convertToDTO);
    }
}
