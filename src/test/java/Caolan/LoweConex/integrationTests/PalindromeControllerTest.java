package Caolan.LoweConex.integrationTests;

import Caolan.LoweConex.model.Response;
import Caolan.LoweConex.service.PalindromeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class PalindromeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PalindromeService palindromeService;

    @Test
    public void testCheckPalindrome() throws Exception {
        // Arrange
        String text = "racecar";
        // Using Mockito to mock the behavior of the palindromeService.checkPalindrome() method
        // Instructing the mock to return a Response object with the message "The text is a palindrome" when called with the text variable as an argument
        when(palindromeService.checkPalindrome(text)).thenReturn(new Response("The text is a palindrome"));

        // Act
        // Using the Spring MockMvc framework to simulate a GET request to the /checkPalindrome endpoint with the text variable as a parameter
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/checkPalindrome").param("text", text));

        // Assert
        resultActions.andExpect(status().isOk())
                // Checking that the response body contains the expected JSON
                .andExpect(jsonPath("$.string", is(text)))
                .andExpect(jsonPath("$.result", is("The text is a palindrome")));
    }
}



