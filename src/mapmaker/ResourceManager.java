package mapmaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ResourceManager{
    private static final ResourceManager RESOURCE_MANAGER = new ResourceManager();
    
    private final String RESOURCE_PATH = "resources/";
    private final String CSS_PATH = RESOURCE_PATH + "css/";
    private final String TXT_PATH = RESOURCE_PATH + "txts/";
    
    private ResourceManager(){
    }
    
    public String loadTxtToString(String fileName) throws FileNotFoundException{
        String txtString = new String();
        Scanner scanner = new Scanner(new File(TXT_PATH + fileName));
        while(scanner.hasNextLine()){
            txtString += scanner.nextLine() + System.getProperty("line.separator");
        }
        return txtString;
    }
    
    public String getCSSUri(String fileName) throws FileNotFoundException{
        File f = new File(CSS_PATH + fileName);
        if(!f.exists())
            throw new FileNotFoundException("File couldn't be located at " +
                    System.getProperty("user.dir") + "/" + CSS_PATH + fileName);
        return f.toURI().toString();
    }
    
    public static ResourceManager get(){
        return RESOURCE_MANAGER;
    }
}
