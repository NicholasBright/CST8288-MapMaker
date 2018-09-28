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
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mapmaker.tool.Tool;
import mapmaker.tool.ToolState;

/**
 *
 * @author owner
 */
public class MapMaker extends Application {
    private Scene                   rootScene;
    private BorderPane              rootPane;
    private MenuBar                 menuBar;
    private ToolBar                 statusBar;
    private ToolBar                 toolsBar;
    private VBox                    detailsBox;
    private ObservableList<String>  messages;
    
    private static boolean GuiFlag = false;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for(String arg : args){
            if(arg.equals("-GC"))
                GuiFlag = true;
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        rootScene = new Scene(buildRootPane(), 800, 600);
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
    
    public Pane buildRootPane(){
        rootPane = new BorderPane();

        menuBar = createMenuBar(
            createMenu("File",
                createMenuItem("New", (e) -> {
                    MapArea.reset();
                }),
                createMenuItem("Save", (e) -> {
                }),
                createMenuItem("Reload Style", (e) -> {
                    loadStylesheet();
                }, createLabel(null, "CSS-icon")),
                new SeparatorMenuItem(),
                createMenuItem("Exit", (e) -> {
                    Platform.exit();
                })
            ),
            createMenu("Help",
                createMenuItem("Credit", new EventHandler<ActionEvent>() {
                    Alert alert;
                    @Override
                    public void handle(ActionEvent e) {
                        if(alert == null){
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Credits");
                            alert.setHeaderText("Credits");
                            try {
                                alert.setContentText( ResourceLoader.loadTxtToString("credits.txt") );
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, ex.toString(), ex);
                                alert.setContentText( "Credits file not found. Report error to developer." );
                                alert.showAndWait();
                                alert = null;
                                return;
                            }
                        }
                        alert.showAndWait();
                    }
                }),
                createMenuItem("Info",new EventHandler<ActionEvent>() {
                    Alert alert = null;
                    @Override
                    public void handle(ActionEvent e) {
                        if(alert == null){
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Info");
                            alert.setHeaderText("Info");
                            try {
                                alert.setContentText( ResourceLoader.loadTxtToString("info.txt") );
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, ex.toString(), ex);
                                alert.setContentText( "Info file not found. Report error to developer." );
                                alert.showAndWait();
                                alert = null;
                                return;
                            }
                        }
                        alert.showAndWait();
                    }
                }),
                new SeparatorMenuItem(),
                createMenuItem("Help",new EventHandler<ActionEvent>() {
                    Alert alert = null;
                    @Override
                    public void handle(ActionEvent e) {
                        if(alert == null){
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Help");
                            alert.setHeaderText("Help");
                            try {
                                alert.setContentText( ResourceLoader.loadTxtToString("help.txt") );
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, ex.toString(), ex);
                                alert.setContentText( "Help file not found. Report error to developer." );
                                alert.showAndWait();
                                alert = null;
                                return;
                            }
                        }
                        alert.showAndWait();
                    }
                })
            )
        );
        
        //Setting up the special label to show selected tool
        Label selectedName = createLabel(null, "SelectedToolLabel");
        ToolState.getToolState().getActiveToolProperty().addListener( (ObservableValue<? extends Tool> observable, Tool oldValue, Tool newValue) -> {
            selectedName.setText(newValue.getNameProperty().get());
        });
        
        statusBar = createToolBar( "StatusBar",
                createLabel("Selected: ", null),
                selectedName,
                new Separator(),
                createLabel("Messages: ", null),
                new ListView(messages)
        );
        
        rootPane.setTop(menuBar);
        rootPane.setBottom(statusBar);
        //rootPane.setLeft(toolsBar);
        //rootPane.setRight(detailsBox());
        //rootPane.setCenter(MapArea.initPane());
        return rootPane;
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
        MenuItem mi = new MenuItem(name, createLabel(null,name+"-icon"));
        mi.setOnAction(handler);
        return mi;
    }
    
    public MenuItem createMenuItem(String name, EventHandler<ActionEvent> handler, Label iconLabel){
        MenuItem mi = new MenuItem(name, iconLabel);
        mi.setOnAction(handler);
        return mi;
    }
    
    public Label createLabel(String text, String id){
        Label l = new Label();
        l.setId(id);
        l.setText(text);
        return l;
    }
    
    public ToolBar createToolBar(String id, Node ... items){
        ToolBar tb = new ToolBar(items);
        tb.setId(id);
        return tb;
    }
}
