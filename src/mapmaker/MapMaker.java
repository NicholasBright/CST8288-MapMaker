/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import java.io.FileNotFoundException;
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
        LOGGER.info("Start of method");
        
        LOGGER.info("Creating scene");
        rootScene = new Scene(GuiController.createRootPane(), 800, 600);
        
        LOGGER.info("Setting stage properties");
        primaryStage.setScene(rootScene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("CST8288 - Map Maker");
        
        LOGGER.info("Loading stylesheet");
        loadStylesheet();
        
        LOGGER.info("Showing stage");
        primaryStage.show();
        LOGGER.info("End of method");
    }
    
    
    @Override
    public void initiateLogging(){
        LogData.initiateLogging(this.getClass().getSimpleName(), LOGGER);
        LOGGER.info("Logging started");
    }
    
    public static void loadStylesheet(){
        LOGGER.info("Start of method");
        LOGGER.info("Clearing old sheets");
        rootScene.getStylesheets().clear();
        
        LOGGER.info("Readding sheets");
        try {
            rootScene.getStylesheets().add( ResourceLoader.getCSSUri("style.css"));
        }
        catch (FileNotFoundException e){
            LOGGER.log(Level.WARNING, "CSS sheet couldn't be found", e);
        }
        LOGGER.info("End of method");
    }
}
