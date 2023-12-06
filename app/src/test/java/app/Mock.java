package app;

import static com.mongodb.client.model.Updates.unset;

public class Mock {

    public class filterMealtype{
        public String filter;
        
        filterMealtype(String f){
            filter = f;
        }

        String displayMealtype(){
            return filter;
        }

    }
    
    
    public class DALLE{
        public String prompt;

        DALLE(String p){
            prompt = p;
        }

        String generatePrompt(){
            return "generated image with: " + prompt + " as the prompt";
        }
    }

    public class Whisper {
        public String mealType;
        public String ingredients;

        Whisper(String m, String i){
            mealType = m;
            ingredients = i;
        }

        String generateWhisper(){
            return "Make me a " + mealType + " recipe with " + ingredients;
        }
    }

    
    public class ShareLinkMock{
        public String user;
        public String Recipe;

        ShareLinkMock(String u, String r){
            user = u;
            Recipe = r;
        }

        String getWebString(){
            String response = "";
            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder
              .append("<html>")
              .append("<body>")
              .append("<h1>")
              .append("The recipe you have selected is." + Recipe)
              .append("under user" + user)
              .append("</h1>")
              .append("</body>")
              .append("</html>");
      
            // encode HTML content
            response = htmlBuilder.toString();
            return response;
        }
    }
}


