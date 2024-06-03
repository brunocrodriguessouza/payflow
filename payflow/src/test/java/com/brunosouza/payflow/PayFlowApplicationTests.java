package com.brunosouza.payflow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PayFlowApplicationTests {

	@Test
	void contextLoads() {
		assertDoesNotThrow(() -> PayFlowApplication.main(new String[]{}));
	}
}
