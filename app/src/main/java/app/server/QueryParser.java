package app.server;

import java.util.HashMap;
import java.util.Map;

// Parse query that follows route/
public class QueryParser {

    public static Map<String, String> parseQuery(String query)  { 

        String[] params = query.split("&");  
        Map<String, String> paramMap = new HashMap<String, String>(); 

        for (String param : params)  {  
            String [] queryPair = param.split("=");
            String key = queryPair[0];  
            if (queryPair.length>1)  {
                String value = queryPair[1];  
                paramMap.put(key, value);
            }  
        }  
        return paramMap;  
    } 

}
