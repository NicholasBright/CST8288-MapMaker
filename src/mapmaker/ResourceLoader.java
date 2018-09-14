/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author nick
 */
public class ResourceLoader {
    public static String loadTxtToString(String path){
        /**
         * TODO - Update function to
         **/
        String txtString = new String();
        try (Scanner scanner = new Scanner(new File(path));) {
            while(scanner.hasNextLine()){
                txtString += scanner.nextLine() + System.getProperty("line.separator");
            }
        } catch (IOException ex) {
            txtString = ex.toString();
        }
        return txtString;
    }
}
