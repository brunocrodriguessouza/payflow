package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.AccountRepository;
import com.brunosouza.payflow.domain.account.Status;
import com.brunosouza.payflow.infraestructure.AccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.security.auth.login.AccountNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.brunosouza.payflow.infraestructure.AccountMapper.convertToDTO;
import static com.brunosouza.payflow.infraestructure.AccountMapper.convertToEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UpdateAccountUseCaseTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private UpdateAccountUseCase updateAccountUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandle_AccountExists() throws AccountNotFoundException {
        // Given
        Long accountId = 1L;
        AccountDTO accountDTO = AccountDTO.builder()
                .id(accountId)
                .dueDate(LocalDate.of(2022, 6, 1))
                .paymentDate(LocalDate.of(2022, 6, 2))
                .value(BigDecimal.valueOf(100))
                .description("Test Account")
                .status(Status.PAID)
                .build();

        Account account = Account.builder()
                .id(accountId)
                .dueDate(accountDTO.getDueDate())
                .paymentDate(accountDTO.getPaymentDate())
                .value(accountDTO.getValue())
                .description(accountDTO.getDescription())
                .status(accountDTO.getStatus().toString())
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // When
        AccountDTO result = updateAccountUseCase.handle(accountId, accountDTO);

        // Then
        assertEquals(accountDTO, result);
    }

    @Test
    void testHandle_AccountNotFound() {
        // Given
        Long accountId = 1L;
        AccountDTO accountDTO = AccountDTO.builder()
                .dueDate(LocalDate.of(2022, 6, 1))
                .paymentDate(LocalDate.of(2022, 6, 2))
                .value(BigDecimal.valueOf(100))
                .description("Test Account")
                .status(Status.PAID)
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Then
        assertThrows(AccountNotFoundException.class, () -> updateAccountUseCase.handle(accountId, accountDTO));
    }
}