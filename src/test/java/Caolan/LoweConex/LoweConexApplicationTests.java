package Caolan.LoweConex;

import Caolan.LoweConex.service.PalindromeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LoweConexApplicationTests {

	@Test
	void contextLoads() {
	}

	// Test for PalindromeChecker.java
	@Test
	void isPalindromeTest(){
		// Arrange
		JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
		CacheManager cacheManager = Mockito.mock(CacheManager.class);
		PalindromeService palindromeChecker = new PalindromeService(jdbcTemplate, cacheManager);

		// Act & Assert
		assertTrue(palindromeChecker.isPalindrome("racecar"));
		assertFalse(palindromeChecker.isPalindrome("hello"));
		assertFalse(palindromeChecker.isPalindrome("567"));
		assertFalse(palindromeChecker.isPalindrome("1234"));
		assertFalse(palindromeChecker.isPalindrome("  "));
		assertTrue(palindromeChecker.isPalindrome("civic"));
	}
}




