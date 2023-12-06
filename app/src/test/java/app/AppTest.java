/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package app;

import org.junit.jupiter.api.Test;

import com.mongodb.internal.connection.Server;

import app.client.App;
import app.client.views.*;
import app.client.controllers.*;
import app.client.Model;
import app.server.ChatGPTHandler;
import app.server.ServerChecker;
import app.server.MyServer;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.net.*;

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
        String response = model.performRequest("POST", null, null, prompt, null, "mockGPT");

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
        String response = refreshTest.performRequest("POST", user, null, prompt, null, "mockGPT");
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
    void dalleLinkGenerationTest() throws IOException{
        MyServer.main(null);
        Model dalleTest =  new Model();
        String recipeTitle = "Bacon Eggs and Ham";

        String url = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fthumb%2Ff%2Ffa%2FHam_and_eggs_over_easy.jpg%2F1200px-Ham_and_eggs_over_easy.jpg&tbnid=jL-bcwE1AkYVvM&vet=12ahUKEwjm75GvxvSCAxWwJEQIHRB_BbYQMygBegQIARBW..i&imgrefurl=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FHam_and_eggs&docid=2WM6ZYnDhyPs5M&w=1200&h=789&q=bacon%20eggs%20and%20ham&ved=2ahUKEwjm75GvxvSCAxWwJEQIHRB_BbYQMygBegQIARBW";

        String response = dalleTest.performRequest("POST", null, null, recipeTitle, null, "mockDalle");
        
        assertEquals(url, response);
        MyServer.stop();

    }

    @Test
    void testGetMealType() throws IOException {
        MyServer.main(null);
        String user = "testGetMealType";
        Model mealtype = new Model();
        String response = mealtype.performRequest("GET", user, null, null, "breakfast", "mealtype");
        // Account with username "testGetMealType" has ONE breakfast recipe named "Egg Bacon and Ham Breakfast Recipe"
        assertEquals(" Bacon and Cheese Jalapeno Frittata;+breakfast", response);
        MyServer.stop();
    }

    // Test /mealtype route to filter lunch recipes that have not been saved
    @Test
    void testGetNoLunchRecipe() throws IOException {
        MyServer.main(null);
        String user = "testGetMealType";
        Model mealtype = new Model();
        String response = mealtype.performRequest("GET", user, null, null, "lunch", "mealtype");
        // Account with username "testGetMealType" has NO lunch recipes
        assertEquals(null, response);
        MyServer.stop();
    }

    // Test /mealtype route to filter the two dinner recipes belonging to "testGetMealType" account
    @Test
    void testGetMultipleDinnerRecipes() throws IOException {
        MyServer.main(null);
        String user = "testGetMealType";
        Model mealtype = new Model();
        String response = mealtype.performRequest("GET", user, null, null, "dinner", "mealtype");
        // Account with username "testGetMealType" has TWO dinner recipes
        assertEquals(" Pancake Bake with Maple Syrup Glaze+dinner_Oven-Baked Salmon with Saffron-Rice:\"+dinner", response);
        MyServer.stop();
    }

    @Test
    void testServerNotRunning() throws IOException{
        boolean status = ServerChecker.isServerRunning("localhost", 8100);
        assertEquals(false, status);
    }
    
    @Test
    void testServerRunning() throws IOException{
        MyServer.main(null);
        boolean status = ServerChecker.isServerRunning("localhost", 8100);
        assertEquals(true, status);
        MyServer.stop();
    }

    @Test
    void sortAlphabeticallyTest() throws IOException { 
        Model sortModelTest = new Model();

        String recipe1 = "B";
        String recipe2 = "A";
        String recipe3 = "C";
        String input = recipe1 + "_" + recipe2 + "_" + recipe3;
        String temp = sortModelTest.sortAlphabetically(input);  
        
        String sorted = recipe2 + "_" + recipe1 + "_" + recipe3;
        assertEquals(temp, sorted);
    }

    @Test
    void sortRAlphabeticallyTest() throws IOException { 
        Model sortModelTest = new Model();

        String recipe1 = "B";
        String recipe2 = "A";
        String recipe3 = "C";
        String input = recipe1 + "_" + recipe2 + "_" + recipe3;
        String temp = sortModelTest.sortRAlphabetically(input);  
        
        String sorted = recipe3 + "_" + recipe1 + "_" + recipe2;
        assertEquals(temp, sorted);
    }

    @Test
    void sortChronologicalTest() throws IOException { 
        Model sortModelTest = new Model();

        String recipe1 = "B";
        String recipe2 = "A";
        String recipe3 = "C";
        String input = recipe1 + "_" + recipe2 + "_" + recipe3;
        String temp = sortModelTest.sortChronological(input);  
        
        String sorted = recipe1 + "_" + recipe2 + "_" + recipe3;
        assertEquals(temp, sorted);
    }

    @Test
    void sortRChronologicalTest() throws IOException { 
        Model sortModelTest = new Model();

        String recipe1 = "B";
        String recipe2 = "A";
        String recipe3 = "C";
        String input = recipe1 + "_" + recipe2 + "_" + recipe3;
        String temp = sortModelTest.sortRChronological(input);  
        
        String sorted = recipe3 + "_" + recipe2 + "_" + recipe1;
        assertEquals(temp, sorted);
    }
}