package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.AccountRepository;
import com.brunosouza.payflow.domain.account.Status;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.security.auth.login.AccountNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChangeStatusUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private ChangeStatusUseCase changeStatusUseCase;

    public ChangeStatusUseCaseTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandle() throws AccountNotFoundException {
        // Given
        Long id = 1L;
        String status = "PAID";

        Account account = Account.builder()
                .dueDate(LocalDate.now())
                .paymentDate(LocalDate.now().plusDays(1))
                .value(BigDecimal.valueOf(100))
                .description("Test Account")
                .status(Status.UNPAID.toString())
                .build();

        AccountDTO expectedAccountDTO = AccountDTO.builder()
                .id(account.getId())
                .dueDate(account.getDueDate())
                .paymentDate(account.getPaymentDate())
                .value(account.getValue())
                .description(account.getDescription())
                .status(Status.PAID)
                .build();

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // When
        AccountDTO result = changeStatusUseCase.handle(id, status);

        // Then
        assertEquals(expectedAccountDTO, result);
        assertEquals(Status.PAID, Status.valueOf(account.getStatus()));
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void testHandle_AccountNotFound() {
        // Given
        Long id = 1L;
        String status = "PAID";

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(AccountNotFoundException.class, () -> {
            changeStatusUseCase.handle(id, status);
        });
    }

}