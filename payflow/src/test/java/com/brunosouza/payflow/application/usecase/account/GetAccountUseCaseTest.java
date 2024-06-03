package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.AccountRepository;
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
    void testGetAccountById() {
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

    @Test
    void testGetAllAccounts() {
        // Given
        Pageable pageable = mock(Pageable.class);
        Page<Account> accountPage = mock(Page.class);


        when(accountRepository.findAll(pageable)).thenReturn(accountPage);
        when(accountPage.map(any())).thenAnswer(invocation -> {
            AccountDTO dto = mock(AccountDTO.class);
            return Page.empty().map(element -> dto);
        });

        // When
        Page<AccountDTO> result = getAccountUseCase.getAllAccounts(pageable);

        // Then
        assertEquals(Page.empty().map(element -> mock(AccountDTO.class)), result);
    }
}