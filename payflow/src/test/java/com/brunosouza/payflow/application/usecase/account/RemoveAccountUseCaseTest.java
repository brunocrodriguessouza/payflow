package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.AccountRepository;
import com.brunosouza.payflow.infraestructure.exception.AccountNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RemoveAccountUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private RemoveAccountUseCase removeAccountUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandle_WhenAccountExists_ShouldDeleteAccount() throws AccountNotFoundException {
        // Given
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // When
        removeAccountUseCase.handle(accountId);

        // Then
        verify(accountRepository, times(1)).deleteById(accountId);
    }

    @Test
    void testHandle_WhenAccountDoesNotExist_ShouldThrowAccountNotFoundException() {
        // Given
        Long accountId = 1L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(AccountNotFoundException.class, () -> removeAccountUseCase.handle(accountId));
    }

}