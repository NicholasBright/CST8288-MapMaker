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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author owner
 */
public class MapMaker extends Application {
    private Scene       rootScene;
    private BorderPane  rootPane;
    private MenuBar     menuBar;
    private ToolBar     statusBar;
    private ToolBar     toolsBar;
    private VBox        detailsBox;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        rootPane = new BorderPane();
        rootScene = new Scene(rootPane, 800, 600);
        
        rootPane.setTop(createMenuBar(
            createMenu("File",
                createMenuItem("New", (e) ->{
                    MapArea.reset();
                }),
                createMenuItem("Exit", (e) -> {
                    Platform.exit();
                })
            )
        ));
        rootPane.setBottom(createToolBar());
        rootPane.setLeft(createToolBar());
        //rootPane.setRight(createDetailsBox());
        
        rootPane.setCenter(MapArea.initPane());
        primaryStage.setScene(rootScene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("CST8288 - Map Maker");
        
        loadStylesheet();
        
        primaryStage.show();
    }
    
    public void loadStylesheet(){
        rootScene.getStylesheets().clear();
        
        try {
            rootScene.getStylesheets().add( ResourceLoader.getCSSUri("style.css"));
        }
        catch (FileNotFoundException e){
            Logger.getLogger(MapMaker.class.getSimpleName()).log(Level.SEVERE, e.toString(), e);
        }
    }
    
    public MenuBar createMenuBar(Menu ... menus){
        MenuBar mb = new MenuBar();
        mb.getMenus().addAll(menus);
        return mb;
    }
    
    public Menu createMenu(String name, MenuItem ... items){
        Menu m = new Menu(name);
        m.getItems().addAll(items);
        return m;
    }
    
    public MenuItem createMenuItem(String name, EventHandler<ActionEvent> handler){
        MenuItem mi = new MenuItem(name, createLabel(null,null,name+"-icon"));
        mi.setOnAction(handler);
        return mi;
    }
    
    public Label createLabel(String name, Node forN, String id){
        Label l = new Label(name, forN);
        l.setId(id);
        return l;
    }
    
    public ToolBar createToolBar(){
        ToolBar tb = new ToolBar();
        return tb;
    }
}
