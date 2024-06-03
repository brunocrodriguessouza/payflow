package com.brunosouza.payflow.presentation.controller;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.application.usecase.account.*;
import com.brunosouza.payflow.infraestructure.exception.AccountNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.math.BigDecimal;


@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final GetAccountUseCase getAccountUseCase;
    private final AddAccountUseCase addAccountUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final ChangeStatusUseCase changeStatusUseCase;
    private final ImportCSVUseCase importCSVUseCase;
    private final RemoveAccountUseCase removeAccountUseCase;
    private final FindByDueDateAndDescriptionUseCase findByDueDateAndDescriptionUseCase;
    private final FindTotalValuePaidByPeriodUseCase findTotalValuePaidByPeriodUseCase;

    public AccountController(GetAccountUseCase getAccountUseCase,
                             AddAccountUseCase addAccountUseCase,
                             UpdateAccountUseCase updateAccountUseCase,
                             ChangeStatusUseCase changeStatusUseCase,
                             ImportCSVUseCase importCSVUseCase,
                             RemoveAccountUseCase removeAccountUseCase,
                             FindByDueDateAndDescriptionUseCase findByDueDateAndDescriptionUseCase,
                             FindTotalValuePaidByPeriodUseCase findTotalValuePaidByPeriodUseCase) {
        this.getAccountUseCase = getAccountUseCase;
        this.addAccountUseCase = addAccountUseCase;
        this.updateAccountUseCase = updateAccountUseCase;
        this.changeStatusUseCase = changeStatusUseCase;
        this.importCSVUseCase = importCSVUseCase;
        this.removeAccountUseCase = removeAccountUseCase;
        this.findByDueDateAndDescriptionUseCase = findByDueDateAndDescriptionUseCase;
        this.findTotalValuePaidByPeriodUseCase = findTotalValuePaidByPeriodUseCase;
    }

    @PostMapping
    public ResponseEntity<AccountDTO> addAccount(@RequestBody AccountDTO accountDTO) {
        AccountDTO createdAccount = addAccountUseCase.handle(accountDTO);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<AccountDTO>> getAllAccounts(Pageable pageable) throws AccountNotFoundException {
        Page<AccountDTO> accounts = getAccountUseCase.getAllAccounts(pageable);
        if (accounts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) throws AccountNotFoundException {
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
        removeAccountUseCase.handle(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long id, @RequestBody AccountDTO accountDTO) throws AccountNotFoundException {
        AccountDTO accountDTOUpdated = updateAccountUseCase.handle(id, accountDTO);
        return ResponseEntity.ok(accountDTOUpdated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountDTO> changeAccountStatus(@PathVariable Long id, @RequestParam String status) throws AccountNotFoundException {
        AccountDTO changedAccount = changeStatusUseCase.handle(id, status);
        return ResponseEntity.ok(changedAccount);
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

        BigDecimal totalValuePaid = findTotalValuePaidByPeriodUseCase.handle(startDate, endDate);
        return ResponseEntity.ok(totalValuePaid);
    }
}
