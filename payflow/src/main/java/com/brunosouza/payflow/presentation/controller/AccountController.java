package com.brunosouza.payflow.presentation.controller;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.application.usecase.account.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;


@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private GetAccountUseCase getAccountUseCase;

    @Autowired
    private AddAccountUseCase addAccountUseCase;

    @Autowired
    private UpdateAccountUseCase updateAccountUseCase;

    @Autowired
    private ChangeStatusUseCase changeStatusUseCase;

    @Autowired
    private ImportCSVUseCase importCSVUseCase;

    @Autowired
    private RemoveAccountUseCase removeAccountUseCase;

    @Autowired
    private FindByDueDateAndDescriptionUseCase findByDueDateAndDescriptionUseCase;

    @PostMapping
    public ResponseEntity<AccountDTO> addAccount(@RequestBody AccountDTO accountDTO) {
        AccountDTO createdAccount = addAccountUseCase.handle(accountDTO);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<AccountDTO>> getAllAccounts(Pageable pageable) {
        Page<AccountDTO> accounts = getAccountUseCase.getAllAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
        AccountDTO account = getAccountUseCase.getAccountById(id);
        if (account != null) {
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AccountDTO>> findByDueDateAndDescription(
            @RequestParam(required = false) String dueDate,
            @RequestParam(required = false) String description,
            Pageable pageable) {

        Page<AccountDTO> accounts = findByDueDateAndDescriptionUseCase.handle(dueDate, description, pageable);

        return ResponseEntity.ok(accounts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) throws AccountNotFoundException {
        try{
            removeAccountUseCase.handle(id);
            return ResponseEntity.noContent().build();
        }catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long id, @RequestBody AccountDTO accountDTO) {
        try {
            AccountDTO accountDTOUpdated = updateAccountUseCase.handle(id, accountDTO);
            return ResponseEntity.ok(accountDTOUpdated);
        }catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountDTO> changeAccountStatus(@PathVariable Long id, @RequestParam String status) throws AccountNotFoundException {
        AccountDTO changedAccount = changeStatusUseCase.handle(id, status);
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
            importCSVUseCase.handle(file);
            return ResponseEntity.ok("File " + file.getOriginalFilename() + " was uploaded and processed successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Error processing the file: " + e.getMessage());
        }
    }

    @GetMapping("/total-value")
    public ResponseEntity<BigDecimal> findTotalValuePaidByPeriod(@RequestParam String startDate,
                                                                 @RequestParam String endDate) {

        BigDecimal totalValuePaid = getAccountUseCase.findTotalValuePaidByPeriod(startDate, endDate);
        return ResponseEntity.ok(totalValuePaid);
    }
}
