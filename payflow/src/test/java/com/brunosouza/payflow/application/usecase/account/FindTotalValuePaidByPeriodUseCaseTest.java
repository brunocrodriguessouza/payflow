package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.domain.account.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FindTotalValuePaidByPeriodUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private FindTotalValuePaidByPeriodUseCase findTotalValuePaidByPeriodUseCase;

    public FindTotalValuePaidByPeriodUseCaseTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandle() {
        // Given
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 12, 31);
        BigDecimal expectedValue = BigDecimal.valueOf(5000);

        when(accountRepository.findTotalValuePaidByPeriod(startDate, endDate)).thenReturn(expectedValue);

        // When
        BigDecimal result = findTotalValuePaidByPeriodUseCase.handle("2022-01-01", "2022-12-31");

        // Then
        assertEquals(expectedValue, result);
    }

    @Test
    void testHandleWithNullValue() {
        // Given
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 12, 31);

        when(accountRepository.findTotalValuePaidByPeriod(startDate, endDate)).thenReturn(null);

        // When
        BigDecimal result = findTotalValuePaidByPeriodUseCase.handle("2022-01-01", "2022-12-31");

        // Then
        assertEquals(BigDecimal.ZERO, result);
    }

}