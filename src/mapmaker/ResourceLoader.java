/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author nick
 */
public class ResourceLoader {
    public static String loadTxtToString(String path){
        String txtString = new String();
        try (BufferedReader txtReader = new BufferedReader(
                new InputStreamReader(
                        GuiController.class.getResourceAsStream(path)))) {
            String nextLine;
            while ((nextLine = txtReader.readLine()) != null)
                txtString += nextLine + "\n";
        } catch (IOException ex) {
            txtString = ex.toString();
        }
        return txtString;
    }
}
