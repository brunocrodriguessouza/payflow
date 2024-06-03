package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.AccountRepository;
import com.brunosouza.payflow.domain.account.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AddAccountUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AddAccountUseCase addAccountUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandle() {
        // Given
        AccountDTO accountDTO = AccountDTO.builder()
                .dueDate(LocalDate.now())
                .paymentDate(LocalDate.now().plusDays(1))
                .value(BigDecimal.valueOf(100))
                .description("Test Account")
                .status(Status.UNPAID)
                .build();
        Account savedAccount = Account.builder()
                .id(1L)
                .dueDate(accountDTO.getDueDate())
                .paymentDate(accountDTO.getPaymentDate())
                .value(accountDTO.getValue())
                .description(accountDTO.getDescription())
                .status(accountDTO.getStatus().toString())
                .build();

        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        // When
        AccountDTO result = addAccountUseCase.handle(accountDTO);

        // Then
        assertEquals(savedAccount.getId(), result.getId());
        assertEquals(savedAccount.getDueDate(), result.getDueDate());
        assertEquals(savedAccount.getPaymentDate(), result.getPaymentDate());
        assertEquals(savedAccount.getValue(), result.getValue());
        assertEquals(savedAccount.getDescription(), result.getDescription());
        assertEquals(Status.valueOf(savedAccount.getStatus()), result.getStatus());
    }

}