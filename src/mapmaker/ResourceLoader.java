/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 *
 * @author nick
 */
public class ResourceLoader implements LogData{
    
    private static final String RESOURCE_PATH = "resources/";
    private static final String CSS_PATH = RESOURCE_PATH + "css/";
    private static final String TXT_PATH = RESOURCE_PATH + "txts/";
    
    private static final Logger LOGGER = Logger.getLogger(GuiController.class.getSimpleName());
    
    public static String loadTxtToString(String fileName) throws FileNotFoundException{
        String txtString = new String();
        Scanner scanner = new Scanner(new File(TXT_PATH + fileName));
        while(scanner.hasNextLine()){
            txtString += scanner.nextLine() + System.getProperty("line.separator");
        }
        scanner.close();
        return txtString;
    }
    
    public static String getCSSUri(String fileName){
        return new File(CSS_PATH + fileName).toURI().toString();
    }

    @Override
    public void initiateLogging(){
        LogData.initiateLogging(this.getClass().getSimpleName(), LOGGER);
    }
}
