package Caolan.LoweConex.integrationTests;

import Caolan.LoweConex.model.Response;
import Caolan.LoweConex.service.PalindromeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PalindromeServiceTest {

    @Autowired
    private PalindromeService palindromeService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testPalindromeCaching() {
        // Clear cache for a clean test
        clearPalindromeCache();

        // Call method with the same input twice
        palindromeService.checkPalindrome("level");
        palindromeService.checkPalindrome("level");

        // Retrieve the cache and the cached value
        Cache cache = cacheManager.getCache("palindromeCache");
        assertNotNull(cache);

        Response cachedResponse = cache.get("level", Response.class);
        assertNotNull(cachedResponse);

        // Check that the cached result matches the expected result
        assertEquals("The text is a palindrome", cachedResponse.getResult());
    }

    private void clearPalindromeCache() {
        Cache cache = cacheManager.getCache("palindromeCache");
        if (cache != null) {
            cache.clear();
        }
    }
}

