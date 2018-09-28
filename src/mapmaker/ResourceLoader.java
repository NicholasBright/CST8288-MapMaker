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
public class ResourceLoader{
    
    private static final String RESOURCE_PATH = "resources/";
    private static final String CSS_PATH = RESOURCE_PATH + "css/";
    private static final String TXT_PATH = RESOURCE_PATH + "txts/";
    
    public static String loadTxtToString(String fileName) throws FileNotFoundException{
        String txtString = new String();
        Scanner scanner = new Scanner(new File(TXT_PATH + fileName));
        while(scanner.hasNextLine()){
            txtString += scanner.nextLine() + System.getProperty("line.separator");
        }
        return txtString;
    }
    
    public static String getCSSUri(String fileName) throws FileNotFoundException{
        File f = new File(CSS_PATH + fileName);
        if(!f.exists())
            throw new FileNotFoundException("File couldn't be located at " +
                    System.getProperty("user.dir") + "/" + CSS_PATH + fileName);
        return f.toURI().toString();
    }
}
