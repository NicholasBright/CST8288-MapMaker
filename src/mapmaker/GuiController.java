/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author owner
 */
public class GuiController {
    static BorderPane rootPane;
    private static MenuBar menuBar;
    private static ToolBar statusBar;
    private static ToolBar toolsBar;
    
    public static BorderPane createRootPane(){
        rootPane = new BorderPane();
        rootPane.setTop(createMenuBar());
        rootPane.setBottom(createStatusBar());
        rootPane.setLeft(createToolsBar());
        return rootPane;
    }
    
    private static MenuBar createMenuBar(){
        menuBar = new MenuBar();
        menuBar.setId("MenuBar");
        menuBar.getMenus().addAll(createFileMenu(),createHelpMenu());
        return menuBar;
    }
    
    private static Menu createFileMenu(){
        Menu fileMenu = new Menu("File");
        fileMenu.setId("FileMenu");
        
        Label newLabel = new Label();
        newLabel.setId("New-icon");
        MenuItem newMenuItem = new MenuItem("New", newLabel);
        newMenuItem.setDisable(true);
        newMenuItem.setId("New");
        
        Label saveLabel = new Label();
        saveLabel.setId("Save-icon");
        MenuItem saveMenuItem = new MenuItem("Save", saveLabel);
        saveMenuItem.setDisable(true);
        saveMenuItem.setId("Save");
        
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        
        Label exitLabel = new Label();
        exitLabel.setId("Exit-icon");
        MenuItem exitMenuItem = new MenuItem("Exit", exitLabel);
        exitMenuItem.setOnAction( e -> {
            Platform.exit();
        });
        exitMenuItem.setId("Exit");
        
        fileMenu.getItems().addAll(newMenuItem, 
            saveMenuItem,
            separatorMenuItem,
            exitMenuItem);
        
        return fileMenu;
    }
    
    private static Menu createHelpMenu(){
        Menu helpMenu = new Menu("Help");
        helpMenu.setId("HelpMenu");
        
        Label creditLabel = new Label();
        creditLabel.setId("Credit-icon");
        MenuItem creditMenuItem = new MenuItem("Credit", creditLabel);
        creditMenuItem.setOnAction( e -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Credits");
            alert.setHeaderText("Credits");
            alert.setContentText( ResourceLoader.loadTxtToString("resources/icons/credits.txt") );
            alert.showAndWait();
        });
        
        Label infoLabel = new Label();
        infoLabel.setId("Info-icon");
        MenuItem infoMenuItem = new MenuItem("Info", infoLabel);
        infoMenuItem.setOnAction( e -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText("Info");
            alert.setContentText( ResourceLoader.loadTxtToString("resources/icons/info.txt") );
            alert.showAndWait();
        });
        
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        
        Label helpLabel = new Label();
        helpLabel.setId("Help-icon");
        MenuItem helpMenuItem = new MenuItem("Help", helpLabel);
        helpMenuItem.setOnAction( e -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Help");
            alert.setHeaderText("Help");
            alert.setContentText( ResourceLoader.loadTxtToString("resources/icons/help.txt") );
            alert.showAndWait();
        });
        
        helpMenu.getItems().addAll(creditMenuItem, infoMenuItem, separatorMenuItem, helpMenuItem);
        
        return helpMenu;
    }
    
    private static ToolBar createStatusBar(){
        statusBar = new ToolBar();
        Label selectedName = new Label("None");
        selectedName.setId("NameOfSelectedTool");
        statusBar.getItems().addAll(new Label("Selected: "), selectedName, new Separator());
        return statusBar;
    }
    
    private static void updateSelected(String newName){
        Label selectedName = (Label)statusBar.lookup("#NameOfSelectedTool");
        selectedName.setText(newName);
    }
    
    private static ToolBar createToolsBar(){
        toolsBar = new ToolBar();
        toolsBar.setOrientation(Orientation.VERTICAL);
        toolsBar.setId("ToolBar");
        
        Button selectToolButton = new Button();
        selectToolButton.setId("Select");
        selectToolButton.setOnAction( e -> {
            updateSelected("Select");
        });
        
        toolsBar.getItems().addAll(selectToolButton);
        
        return toolsBar;
    }
}
