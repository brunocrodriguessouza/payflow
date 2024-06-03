package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.AccountRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ImportCSVUseCase {

    @Autowired
    private AccountRepository accountRepository;

    public void handle(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

            for (CSVRecord record : csvParser) {
                Account account = parseAccount(record);
                accountRepository.save(account);
            }
        }
    }

    private Account parseAccount(CSVRecord record) {
        List<String> values = record.stream()
                .map(value -> Normalizer.normalize(value.trim(), Normalizer.Form.NFD))
                .toList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate dueDate = LocalDate.parse(values.get(0), formatter);
        LocalDate paymentDate = LocalDate.parse(values.get(1), formatter);

        return Account.builder()
                .dueDate(dueDate)
                .paymentDate(paymentDate)
                .value(new BigDecimal(values.get(2)))
                .description(values.get(3))
                .status(values.get(4))
                .build();
    }
}
