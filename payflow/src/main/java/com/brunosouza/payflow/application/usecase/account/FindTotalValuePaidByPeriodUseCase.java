package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.domain.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class FindTotalValuePaidByPeriodUseCase {

    @Autowired
    private AccountRepository accountRepository;

    public BigDecimal handle(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDt = LocalDate.parse(startDate, formatter);
        LocalDate endDt = LocalDate.parse(endDate, formatter);

        BigDecimal totalValue = accountRepository.findTotalValuePaidByPeriod(startDt, endDt);
        return totalValue != null ? totalValue : BigDecimal.ZERO;
    }
}
