package app;

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
}


