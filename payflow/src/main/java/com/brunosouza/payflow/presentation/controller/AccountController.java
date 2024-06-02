package com.brunosouza.payflow.presentation.controller;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.application.service.AccountService;
import com.brunosouza.payflow.application.usecase.AccountUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;


@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountUseCase accountUseCase;

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        AccountDTO createdAccount = accountUseCase.createAccount(accountDTO);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<AccountDTO>> getAllAccounts(Pageable pageable) {
        Page<AccountDTO> accounts = accountUseCase.getAllAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
        AccountDTO account = accountUseCase.getAccountById(id);
        if (account != null) {
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AccountDTO>> findByDueDateAndDescription(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
            @RequestParam(required = false) String description,
            Pageable pageable) {

        Page<AccountDTO> accounts = accountUseCase.findByDueDateAndDescription(dueDate, description, pageable);

        return ResponseEntity.ok(accounts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountUseCase.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/change-status")
    public ResponseEntity<AccountDTO> changeAccountStatus(@PathVariable Long id, @RequestParam String status) {
        AccountDTO changedAccount = accountUseCase.changeAccountStatus(id, status);
        if (changedAccount != null) {
            return ResponseEntity.ok(changedAccount);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File cannot be empty");
        }

        try {
            accountService.importAccountsFromCSV(file);
            return ResponseEntity.ok("File " + file.getOriginalFilename() + " was uploaded and processed successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the file: " + e.getMessage());
        }
    }
}
