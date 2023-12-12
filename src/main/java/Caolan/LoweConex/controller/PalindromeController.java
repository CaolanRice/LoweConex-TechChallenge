package Caolan.LoweConex.controller;

import Caolan.LoweConex.service.PalindromeService;
import Caolan.LoweConex.model.Request;
import Caolan.LoweConex.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
public class PalindromeController {

    private final PalindromeService palindromeService;

    @Autowired
    public PalindromeController(PalindromeService palindromeService) {
        this.palindromeService = palindromeService;
    }

    @GetMapping("/checkPalindrome")
    public ResponseEntity<Map<String, String>> checkPalindrome(@RequestParam String text) {
        // Check if the input string is a palindrome using the service
        Response response = palindromeService.checkPalindrome(text);

        // Prepare response
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("string", text);
        responseBody.put("result", response.getResult());

        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/clear-database")
    public ResponseEntity<String> clearDatabase() {
        palindromeService.clearDatabase();
        return ResponseEntity.ok("Database cleared successfully");
    }

}