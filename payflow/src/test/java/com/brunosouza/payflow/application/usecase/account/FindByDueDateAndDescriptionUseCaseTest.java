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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FindByDueDateAndDescriptionUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private FindByDueDateAndDescriptionUseCase findByDueDateAndDescriptionUseCase;

    public FindByDueDateAndDescriptionUseCaseTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandle() {
        // Given
        String dueDate = "2022-06-01";
        String description = "Test description";
        Pageable pageable = mock(Pageable.class);
        List<Account> accounts = new ArrayList<>();
        Account account = new Account();
        accounts.add(account);
        Page<Account> accountPage = mock(Page.class);
        Page<AccountDTO> expectedPage = mock(Page.class);

        when(accountRepository.findByDueDateAndDescriptionContaining(
                LocalDate.parse(dueDate), description, pageable)).thenReturn(accountPage);
        when(accountPage.map(any())).thenAnswer(invocation -> {
            AccountDTO dto = mock(AccountDTO.class);
            return Page.empty().map(element -> dto);
        });

        // When
        Page<AccountDTO> result = findByDueDateAndDescriptionUseCase.handle(dueDate, description, pageable);

        // Then
        assertEquals(Page.empty().map(element -> mock(AccountDTO.class)), result);
    }

}