package com.brunosouza.payflow.application.usecase.account;

import com.brunosouza.payflow.domain.account.Account;
import com.brunosouza.payflow.domain.account.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.*;

class ImportCSVUseCaseTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private ImportCSVUseCase importCSVUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandle() throws IOException {
        // Given
        String csvData = "10/05/2024,11/05/2024,100,Test Account,PENDING\n" +
                "11/05/2024,12/05/2024,200,Another Account,PAID";
        MultipartFile mockFile = createMockMultipartFile("test.csv", csvData);

        // When
        importCSVUseCase.handle(mockFile);

        // Then
        verify(accountRepository, times(2)).save(any(Account.class));
    }

    private MockMultipartFile createMockMultipartFile(String filename, String content) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        return new MockMultipartFile(filename, filename, null, inputStream);
    }

}