/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author owner
 */
public class MapMaker extends Application implements LogData {
    static Scene rootScene;
    static boolean logging = false;
    static boolean allLogging = false;
    private static final Logger LOGGER = Logger.getLogger(MapMaker.class.getName());
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int iter = 0;
        for(String arg: args){
            if(arg.startsWith("-") && arg.length() > 1){
                for(char c : arg.substring(1).toCharArray()){
                    switch(c){
                        case 'l':
                            logging = true;
                            break;
                        case 'a':
                            allLogging = true;
                            break;
                        case 'L':
                            logging = allLogging = true;
                            break;
                    }
                }
            }
        }
        
        new MapMaker().initiateLogging();
        new GuiController().initiateLogging();
        new ResourceLoader().initiateLogging();
        
        LOGGER.log(Level.INFO,"Launching app");
        
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
            rootScene.getStylesheets().add( ResourceLoader.getCSSUri("style.css"));
        }
        catch(Exception e){
            System.out.print(e);
        }
        
        primaryStage.show();
    }
    
    
    @Override
    public void initiateLogging(){
        LogData.initiateLogging(this.getClass().getSimpleName(), LOGGER);
    }
}
