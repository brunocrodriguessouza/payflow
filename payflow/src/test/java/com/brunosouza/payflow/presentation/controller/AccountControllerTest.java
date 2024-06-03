package com.brunosouza.payflow.presentation.controller;

import com.brunosouza.payflow.application.dto.AccountDTO;
import com.brunosouza.payflow.application.usecase.account.*;
import com.brunosouza.payflow.infraestructure.exception.AccountNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @Mock
    private GetAccountUseCase getAccountUseCase;

    @Mock
    private AddAccountUseCase addAccountUseCase;

    @Mock
    private UpdateAccountUseCase updateAccountUseCase;

    @Mock
    private ChangeStatusUseCase changeStatusUseCase;

    @Mock
    private ImportCSVUseCase importCSVUseCase;

    @Mock
    private RemoveAccountUseCase removeAccountUseCase;

    @Mock
    private FindByDueDateAndDescriptionUseCase findByDueDateAndDescriptionUseCase;

    @Mock
    private FindTotalValuePaidByPeriodUseCase findTotalValuePaidByPeriodUseCase;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddAccount() {
        // Given
        AccountDTO accountDTO = new AccountDTO();
        when(addAccountUseCase.handle(accountDTO)).thenReturn(accountDTO);

        // When
        ResponseEntity<AccountDTO> responseEntity = accountController.addAccount(accountDTO);

        // Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(accountDTO, responseEntity.getBody());
    }

    @Test
    void testGetAllAccounts() {
        // Given
        Pageable pageable = mock(Pageable.class);
        Page<AccountDTO> accountPage = mock(Page.class);
        when(getAccountUseCase.getAllAccounts(pageable)).thenReturn(accountPage);

        // When
        ResponseEntity<Page<AccountDTO>> responseEntity = accountController.getAllAccounts(pageable);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(accountPage, responseEntity.getBody());
    }

    @Test
    void testGetAccountById() {
        // Given
        Long id = 1L;
        AccountDTO accountDTO = new AccountDTO();
        when(getAccountUseCase.getAccountById(id)).thenReturn(accountDTO);

        // When
        ResponseEntity<AccountDTO> responseEntity = accountController.getAccountById(id);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(accountDTO, responseEntity.getBody());
    }

    @Test
    void testFindByDueDateAndDescription() {
        // Given
        Pageable pageable = mock(Pageable.class);
        Page<AccountDTO> accountPage = mock(Page.class);
        when(findByDueDateAndDescriptionUseCase.handle(anyString(), anyString(), eq(pageable))).thenReturn(accountPage);

        // When
        ResponseEntity<Page<AccountDTO>> responseEntity = accountController.findByDueDateAndDescription("2022-01-01", "test", pageable);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(accountPage, responseEntity.getBody());
    }

    @Test
    void testDeleteAccount() throws AccountNotFoundException {
        // Given
        Long id = 1L;

        // When
        ResponseEntity<Void> responseEntity = accountController.deleteAccount(id);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateAccount() throws AccountNotFoundException {
        // Given
        Long id = 1L;
        AccountDTO accountDTO = new AccountDTO();
        when(updateAccountUseCase.handle(id, accountDTO)).thenReturn(accountDTO);

        // When
        ResponseEntity<AccountDTO> responseEntity = accountController.updateAccount(id, accountDTO);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(accountDTO, responseEntity.getBody());
    }

    @Test
    void testChangeAccountStatus() throws AccountNotFoundException {
        // Given
        Long id = 1L;
        String status = "PAID";
        AccountDTO accountDTO = new AccountDTO();
        when(changeStatusUseCase.handle(id, status)).thenReturn(accountDTO);

        // When
        ResponseEntity<AccountDTO> responseEntity = accountController.changeAccountStatus(id, status);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(accountDTO, responseEntity.getBody());
    }

    @Test
    void testFindTotalValuePaidByPeriod() {
        // Given
        String startDate = "2022-01-01";
        String endDate = "2022-01-31";
        BigDecimal totalValuePaid = BigDecimal.TEN;
        when(findTotalValuePaidByPeriodUseCase.handle(startDate, endDate)).thenReturn(totalValuePaid);

        // When
        ResponseEntity<BigDecimal> responseEntity = accountController.findTotalValuePaidByPeriod(startDate, endDate);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, totalValuePaid.compareTo(responseEntity.getBody()));
    }

    @Test
    void testHandleFileUpload() throws IOException {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);

        // When
        ResponseEntity<String> responseEntity = accountController.handleFileUpload(file);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteAccountNotFound() throws AccountNotFoundException {
        // Given
        Long id = 1L;
        doThrow(AccountNotFoundException.class).when(removeAccountUseCase).handle(id);

        // When
        ResponseEntity<Void> responseEntity = accountController.deleteAccount(id);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testGetAccountByIdNotFound() {
        // Given
        Long id = 1L;
        when(getAccountUseCase.getAccountById(id)).thenReturn(null);

        // When
        ResponseEntity<AccountDTO> responseEntity = accountController.getAccountById(id);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateAccountNotFound() throws AccountNotFoundException {
        // Given
        Long id = 1L;
        AccountDTO accountDTO = new AccountDTO();
        doThrow(AccountNotFoundException.class).when(updateAccountUseCase).handle(id, accountDTO);

        // When
        ResponseEntity<AccountDTO> responseEntity = accountController.updateAccount(id, accountDTO);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testChangeAccountStatusNotFound() throws AccountNotFoundException {
        // Given
        Long id = 1L;
        String status = "paid";
        when(changeStatusUseCase.handle(id, status)).thenReturn(null);

        // When
        ResponseEntity<AccountDTO> responseEntity = accountController.changeAccountStatus(id, status);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testHandleFileUploadEmptyFile() {
        // Given
        MultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", new byte[0]);

        // When
        ResponseEntity<String> responseEntity = accountController.handleFileUpload(file);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("File cannot be empty", responseEntity.getBody());
    }

    @Test
    void testHandleFileUploadException() throws IOException {
        // Given
        MultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "content".getBytes());
        IOException exception = new IOException("Error processing the file");
        doThrow(exception).when(importCSVUseCase).handle(file);

        // When
        ResponseEntity<String> responseEntity = accountController.handleFileUpload(file);

        // Then
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertEquals("Error processing the file: " + exception.getMessage(), responseEntity.getBody());
    }

}