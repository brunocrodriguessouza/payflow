package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.AccountRepository;
import com.brunosouza.payflow.infraestructure.exception.AccountNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetAccountUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private GetAccountUseCase getAccountUseCase;

    public GetAccountUseCaseTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAccountById() throws AccountNotFoundException {
        // Given
        Long accountId = 1L;
        Account account = Account.builder()
                .id(accountId)
                .dueDate(LocalDate.now())
                .paymentDate(LocalDate.now().plusDays(1))
                .value(BigDecimal.valueOf(100))
                .description("Test Account")
                .status("UNPAID")
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // When
        AccountDTO result = getAccountUseCase.getAccountById(accountId);

        // Then
        assertEquals(accountId, result.getId());
    }

}