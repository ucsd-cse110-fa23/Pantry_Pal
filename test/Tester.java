import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import javafx.scene.Scene;
import javafx.stage.Stage;


public class Tester {
    
    @Test
    public void testMealType(){
        String mealtype = MealType.mealType("This is what I want for breakfast, please give me a good recipe");
        String mealtype2 = MealType.mealType("This is what I want for dinner, please give me a good recipe");
        String mealtype3 = MealType.mealType("This is what I want for lunch, please give me a good recipe");
        String mealtype4 = MealType.mealType("I want food");
        assertEquals("breakfast", mealtype);
        assertEquals("dinner", mealtype2);
        assertEquals("lunch", mealtype3);
        assertEquals(null, mealtype4);


    }

    @Test
    public void testTranscribeIngredients() throws IOException, URISyntaxException{

        Ingredients.transcribeIngredients();
        assertEquals("Pork chop.", Ingredients.ingredientsString);
    }

    // cannot test with the new parameters, needs UI objects to test
    // @Test
    // public void testChatGPT() throws  InterruptedException, IOException, URISyntaxException{
    //     ChatGPT newOne = new ChatGPT("lunch", "onions, eggs, tomatoes", 300, primaryStage, homeScene, recipeList);
    //     assertNotNull(newOne);
    // }

}
