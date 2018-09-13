/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import java.io.File;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author owner
 */
public class MapMaker extends Application {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(GuiController.createRootPane(), 600, 400);
        
        try{
            String css = MapMaker.class.getResource("resources/css/style.css").toExternalForm(); 
            scene.getStylesheets().add(css);
        }
        catch(Exception e){
            System.out.print(e);
        }
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("CST8288 - Map Maker");
        primaryStage.show();
    }
    
}
