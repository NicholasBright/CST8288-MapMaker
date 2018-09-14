/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nick
 */
public class ResourceLoader implements LogData{
    
    private static final String RESOURCE_PATH = "resources/";
    private static final String CSS_PATH = RESOURCE_PATH + "css/";
    private static final String TXT_PATH = RESOURCE_PATH + "txts/";
    
    private static final Logger LOGGER = Logger.getLogger(ResourceLoader.class.getSimpleName());
    
    public static String loadTxtToString(String fileName) throws FileNotFoundException{
        LOGGER.info("Start of method");
        String txtString = new String();
        LOGGER.info("Creating scanner");
        Scanner scanner = new Scanner(new File(TXT_PATH + fileName));
        LOGGER.log(Level.INFO, "Starting look through file ({0}) data", fileName);
        while(scanner.hasNextLine()){
            txtString += scanner.nextLine() + System.getProperty("line.separator");
            LOGGER.info("Successfully retrieved line of data");
        }
        LOGGER.info("End of method");
        return txtString;
    }
    
    public static String getCSSUri(String fileName) throws FileNotFoundException{
        LOGGER.info("Start of method");
        LOGGER.info("Finding CSS file");
        File f = new File(CSS_PATH + fileName);
        if(!f.exists())
            throw new FileNotFoundException("File couldn't be located at " +
                    System.getProperty("user.dir") + "/" + CSS_PATH + fileName);
        LOGGER.info("End of method");
        return f.toURI().toString();
    }

    @Override
    public void initiateLogging(){
        LogData.initiateLogging(this.getClass().getSimpleName(), LOGGER);
        LOGGER.info("Logging started");
    }
}
