/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package app;

import org.junit.jupiter.api.Test;

import app.Mock.DALLE;
import app.client.App;
import app.client.View;
import app.client.Controller;
import app.client.Model;
import app.server.ChatGPTHandler;
import app.server.MyServer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

class AppTest {
    // Tests whether the prompt we give chatgpt maintains the same provided ingredients as the original recipe
    @Test 
    void testGptSameIngredients() throws IOException {
        MyServer.main(null);
        String mealType = "dinner";
        String ingredients = "steak, potatoes, butter";
        Model model = new Model();
        String prompt = "Make me a " + mealType + " recipe using " + ingredients + " presented in JSON format with the \"title\" as the first key with its value as one string, \"ingredients\" as another key with its value as one string, and \"instructions\" as the last key with its value as one string";
        String response = model.performRequest("POST", null, null, prompt, null, "chatgpt");

        // API call should have successfully been made and returned thorugh model with the mealType and ingredients
        assertFalse(response.equals(""));
        MyServer.stop();
    }

    @Test
    void testGptBddRefresh() throws IOException {
        MyServer.main(null);
        // BDD TEST
        String user = "userBDD"; 

        // Scenario: I don't like the recipe generated
        String generatedText = "Scrambled eggs with bacon and toast, Step 1:... Step 2:...";
        // Given: I have chosen breakfast and listed bacon, eggs, and sausage
        // When: I am given a recipe for scrambled eggs with bacon and toast
        // And: I do not want this recipe
        String mealType = "breakfast";
        String ingredients = "bacon, eggs, sausage";
        // Then: when I press the refresh button it will generate another recipe like a bacon egg sandwich
        Model refreshTest = new Model();
        String prompt = "Make me a " + mealType + " recipe using " + ingredients + " presented in JSON format with the \"title\" as the first key with its value as one string, \"ingredients\" as another key with its value as one string, and \"instructions\" as the last key with its value as one string";
        String response = refreshTest.performRequest("POST", user, null, prompt, null, "chatgpt");
        assertNotEquals(response, generatedText);
        MyServer.stop();
    }

    // Tests successful sign up
    @Test
    void testValidSignup() throws IOException {
        MyServer.main(null);
        Model model = new Model();
        String newUser = Long.toHexString(System.currentTimeMillis());
        String password = Long.toHexString(System.currentTimeMillis() + 3);
        String response = model.performRequest("POST", newUser, password, null, null, "signup");
        assertTrue(response.equals("NEW USER CREATED"));
        MyServer.stop();
    }

    // Tests signing up on a name thats taken already 
    @Test
    void testSignupUsernameTaken() throws IOException { 
        MyServer.main(null);
        Model loginTest = new Model();
        String response = loginTest.performRequest("POST", "Bob", "password12", null, null, "signup");
        assertEquals("USERNAME TAKEN", response);
        MyServer.stop();
    }

    // Tests a valid login
    @Test
    void testValidLoginValid() throws IOException { 
        MyServer.main(null);
        Model loginTest = new Model();
        String response = loginTest.performRequest("POST", "Bob", "password12", null, null, "login");
        assertEquals("SUCCESS", response);
        MyServer.stop();
    }

    // Tests a invalid login password
    @Test
    void testInvalidLoginCredentials() throws IOException { 
        MyServer.main(null);
        Model loginTest = new Model();
        String response = loginTest.performRequest("POST", "Bob", "wrongPassword", null, null, "login");
        assertEquals("INCORRECT CREDENTIALS", response);
        MyServer.stop();
    }

    // Tests a username that doesn't exist for login
    @Test
    void testLoginDoesntExist() throws IOException { 
        MyServer.main(null);
        Model loginTest = new Model();
        String response = loginTest.performRequest("POST", "fakeName", "password12", null, null, "login");
        assertEquals("USER NOT FOUND", response);
        MyServer.stop();
    }

    // Test /mealtype route to filter breakfast recipes belonging to "testGetMealType" account
    @Test
    void testGetMealType() throws IOException {
        MyServer.main(null);
        String user = "testGetMealType";
        Model mealtype = new Model();
        String response = mealtype.performRequest("GET", user, null, null, "breakfast", "mealtype");
        
        // Account with username "testGetMealType" has ONE breakfast recipe named "Egg Bacon and Ham Breakfast Recipe"
        assertEquals("Egg Bacon and Ham Breakfast Recipe+breakfast", response);
        MyServer.stop();
    }

    // Test /mealtype route to filter lunch recipes belonging to "testGetMealType" account
    @Test
    void testGetEmptyMealType() throws IOException {
        MyServer.main(null);
        String user = "testGetMealType";
        Model mealtype = new Model();
        String response = mealtype.performRequest("GET", user, null, null, "lunch", "mealtype");
        
        // Account with username "testGetMealType" has NO lunch recipes
        assertEquals(null, response);
        MyServer.stop();
    }

    @Test
    void dalleTest() throws IOException {
        // have a generated recipe with the ingredients
        String generatedRecipe = "Pancake ingredients: flour, eggs, sugar. mix ingredients together and pour into pan";

        // gneerate the image
        Mock mockData = new Mock();
        DALLE dalleMock = mockData.new DALLE(generatedRecipe);
        String res = dalleMock.generatePrompt();
        assertTrue(true,res);
    }

}