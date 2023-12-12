package Caolan.LoweConex.service;

import Caolan.LoweConex.model.Response;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.cache.CacheManager;


import java.util.List;
import java.util.Map;


@Service
@CacheConfig(cacheNames = {"palindromeCache"})
public class PalindromeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PalindromeService.class);
    private final JdbcTemplate jdbcTemplate;
    private final CacheManager cacheManager;  // Inject CacheManager

    @Autowired
    public PalindromeService(JdbcTemplate jdbcTemplate, CacheManager cacheManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.cacheManager = cacheManager;
    }

@Cacheable("palindromeCache")
public Response checkPalindrome(String text) {
    // Check if the text is a palindrome
    boolean isPalindrome = isPalindrome(text);
    String result = isPalindrome ? "The text is a palindrome" : "The text is not a palindrome";

    // Save the response to the H2 database
    saveResponseToDatabase(text, result);

    return new Response(result);
}

    public boolean isPalindrome(String text) {
        // Validate the input
        if (!isValidInput(text)) {
            return false;
        }

        // Remove non-alphanumeric characters and convert to lowercase
        StringBuilder cleanText = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                cleanText.append(Character.toLowerCase(c));
            }
        }
        String reversedText = new StringBuilder(cleanText).reverse().toString();
        return cleanText.toString().equals(reversedText);
    }

    private boolean isValidInput(String text) {
        // Regular expression that matches any string that contains only letters
        String regex = "^[a-zA-Z]*$";
        return text.matches(regex);
    }

    private void saveResponseToDatabase(String text, String result) {
        // Insert the response into the database
        String sql = "INSERT INTO responses (text, result) VALUES (?, ?)";
        jdbcTemplate.update(sql, text, result);
    }

    @Transactional // This annotation ensures that the entire method is run as a single transaction, meaning that if any part of the method fails, the entire method will be rolled back
    public void clearDatabase() {
        try {
            // Clear all data from the 'responses' table
            String sql = "DELETE FROM responses";
            int rowsAffected = jdbcTemplate.update(sql);

            // Confirm the deletion
            if (rowsAffected > 0) {
                LOGGER.info("Successfully deleted " + rowsAffected + " rows from the 'responses' table.");
            } else {
                LOGGER.warn("No rows were deleted from the 'responses' table.");
            }
        } catch (DataAccessException e) {
            LOGGER.error("An error occurred while trying to clear the 'responses' table: " + e.getMessage());
        }
    }

    // This method will be called after the PalindromeService bean is constructed
    @PostConstruct
    @CacheEvict(value = "palindromeCache", allEntries = true)
    public void initializeCache() {
        String sql = "SELECT text, result FROM responses";
        List<Map<String, Object>> responsesInDatabase = jdbcTemplate.queryForList(sql);

        // Get the cache
        Cache palindromeCache = cacheManager.getCache("palindromeCache");

        // Iterate over the responses from the database
        for (Map<String, Object> response : responsesInDatabase) {
            String text = (String) response.get("text");
            String result = (String) response.get("result");

            // Check if the text already exists in the cache
            if (palindromeCache.get(text) == null) {
                // If not, manually put it into the cache without calling checkPalindrome
                palindromeCache.put(text, new Response(result));
            }
        }
    }

}