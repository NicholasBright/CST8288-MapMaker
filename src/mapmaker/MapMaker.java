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
    static Scene rootScene;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        rootScene = new Scene(GuiController.createRootPane(), 800, 600);
        primaryStage.setScene(rootScene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("CST8288 - Map Maker");
        
        try{
            rootScene.getStylesheets().add( (new File("resources/css/style.css")).getPath());
            //String css = MapMaker.class.getResource("resources/css/style.css").toExternalForm(); 
            //rootScene.getStylesheets().add(css);
        }
        catch(Exception e){
            System.out.print(e);
        }
        
        primaryStage.show();
    }
    
}
