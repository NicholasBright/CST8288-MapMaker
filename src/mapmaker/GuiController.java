/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Polygon;
import mapmaker.tool.SelectTool;

/**
 *
 * @author owner
 */
public class GuiController implements LogData {
    static BorderPane rootPane;
    private static MenuBar menuBar;
    private static ToolBar statusBar;
    private static ToolBar toolsBar;
    
    private static final Logger LOGGER = Logger.getLogger(GuiController.class.getSimpleName());
    
    public static BorderPane createRootPane(){
        LOGGER.info("Start of method");
        LOGGER.info("Creating rootPane");
        rootPane = new BorderPane();
        
        LOGGER.info("Populating rootPane");
        rootPane.setTop(createMenuBar());
        rootPane.setBottom(createStatusBar());
        rootPane.setLeft(createToolsBar());
        ToolBar t = new ToolBar();
        t.setOrientation(Orientation.VERTICAL);
        rootPane.setRight(t);
        
        LOGGER.info("Adding Center \"MapArea\" Pane");
        rootPane.setCenter(MapArea.initPane());
        
        LOGGER.info("End of method");
        return rootPane;
    }
    
    private static MenuBar createMenuBar(){
        LOGGER.info("Start of method");
        LOGGER.info("Creating menuBar");
        menuBar = new MenuBar();
        menuBar.setId("MenuBar");
        LOGGER.info("Populating menuBar");
        menuBar.getMenus().addAll(createFileMenu(),createHelpMenu());
        LOGGER.info("End of method");
        return menuBar;
    }
    
    private static Menu createFileMenu(){
        LOGGER.info("Start of method");
        LOGGER.info("Creating fileMenu");
        Menu fileMenu = new Menu("File");
        fileMenu.setId("FileMenu");
        
        LOGGER.info("Creating \"New\" MenuItem");
        Label newLabel = new Label();
        newLabel.setId("New-icon");
        MenuItem newMenuItem = new MenuItem("New", newLabel);
        newMenuItem.setDisable(true);
        newMenuItem.setId("New");
        
        LOGGER.info("Creating \"Save\" MenuItem");
        Label saveLabel = new Label();
        saveLabel.setId("Save-icon");
        MenuItem saveMenuItem = new MenuItem("Save", saveLabel);
        saveMenuItem.setDisable(true);
        saveMenuItem.setId("Save");
        
        LOGGER.info("Creating \"Reload Style\" MenuItem");
        Label reloadStyleLabel = new Label();
        reloadStyleLabel.setId("CSS-icon");
        MenuItem reloadStyleMenuItem = new MenuItem("Reload Style", reloadStyleLabel);
        reloadStyleMenuItem.setOnAction(e -> {
            MapMaker.loadStylesheet();
        });
        
        LOGGER.info("Creating SeparatorMenuItem");
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        
        LOGGER.info("Creating \"Exit\" MenuItem");
        Label exitLabel = new Label();
        exitLabel.setId("Exit-icon");
        MenuItem exitMenuItem = new MenuItem("Exit", exitLabel);
        exitMenuItem.setOnAction( e -> {
            LOGGER.info("Start and end of method");
            Platform.exit();
        });
        exitMenuItem.setId("Exit");
        
        LOGGER.info("Adding items to fileMenu");
        fileMenu.getItems().addAll(newMenuItem, 
            saveMenuItem,
            reloadStyleMenuItem,
            separatorMenuItem,
            exitMenuItem);
        
        LOGGER.info("End of method");
        return fileMenu;
    }
    
    private static Menu createHelpMenu(){
        LOGGER.info("Start of method");
        LOGGER.info("Creating helpMenu");
        Menu helpMenu = new Menu("Help");
        helpMenu.setId("HelpMenu");
        
        LOGGER.info("Creating \"Credit\" MenuItem");
        Label creditLabel = new Label();
        creditLabel.setId("Credit-icon");
        MenuItem creditMenuItem = new MenuItem("Credit", creditLabel);
        creditMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            Alert alert = null;
            
            @Override
            public void handle(ActionEvent e) {
                LOGGER.info("Start of method");
                if(alert == null){
                    LOGGER.info("Creating new \"Credit\" Alert");
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Credits");
                    alert.setHeaderText("Credits");
                    try {
                        alert.setContentText( ResourceLoader.loadTxtToString("credits.txt") );
                    } catch (FileNotFoundException ex) {
                        LOGGER.log(Level.SEVERE, ex.toString(), ex);
                        alert.setContentText( "Credits file not found. Report error to developer." );
                        alert.showAndWait();
                        alert = null;
                        LOGGER.info("End of method");
                        return;
                    }
                }
                LOGGER.info("End of Method");
                alert.showAndWait();
            }
        });
        
        LOGGER.info("Creating \"Info\" MenuItem");
        Label infoLabel = new Label();
        infoLabel.setId("Info-icon");
        MenuItem infoMenuItem = new MenuItem("Info", infoLabel);
        infoMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            Alert alert = null;
            @Override
            public void handle(ActionEvent e) {
                LOGGER.info("Start of method");
                if(alert == null){
                    LOGGER.info("Creating new \"Info\" Alert");
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Info");
                    alert.setHeaderText("Info");
                    try {
                        alert.setContentText( ResourceLoader.loadTxtToString("info.txt") );
                    } catch (FileNotFoundException ex) {
                        LOGGER.log(Level.SEVERE, ex.toString(), ex);
                        alert.setContentText( "Info file not found. Report error to developer." );
                        alert.showAndWait();
                        alert = null;
                        LOGGER.info("End of method");
                        return;
                    }
                }
                LOGGER.info("End of method");
                alert.showAndWait();
            }
        });
        
        LOGGER.info("Creating SeparatorMenuItem");
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        
        LOGGER.info("Creating \"Help\" MenuItem");
        Label helpLabel = new Label();
        helpLabel.setId("Help-icon");
        MenuItem helpMenuItem = new MenuItem("Help", helpLabel);
        helpMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            Alert alert = null;
            @Override
            public void handle(ActionEvent e) {
                LOGGER.info("Start of method");
                if(alert == null){
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Help");
                    alert.setHeaderText("Help");
                    try {
                        alert.setContentText( ResourceLoader.loadTxtToString("help.txt") );
                    } catch (FileNotFoundException ex) {
                        LOGGER.log(Level.SEVERE, ex.toString(), ex);
                        alert.setContentText( "Help file not found. Report error to developer." );
                        alert.showAndWait();
                        alert = null;
                        LOGGER.info("End of method");
                        return;
                    }
                }
                LOGGER.info("End of method");
                alert.showAndWait();
            }
        });
        
        LOGGER.info("Adding items to \"Help\" Menu");
        helpMenu.getItems().addAll(
            creditMenuItem,
            infoMenuItem,
            separatorMenuItem, 
            helpMenuItem);
        
        LOGGER.info("End of method");
        return helpMenu;
    }
    
    private static ToolBar createStatusBar(){
        LOGGER.info("Start of method");
        LOGGER.info("Creating \"statusBar\" ToolBar");
        statusBar = new ToolBar();
        statusBar.setId("StatusBar");
        
        LOGGER.info("Creating \"Selected\" Label");
        Label selectedTitle = new Label("Selected: ");
        selectedTitle.setMinWidth(Label.USE_PREF_SIZE);
        Label selectedName = new Label("None");
        selectedName.setId("NameOfSelectedTool");
        selectedName.setMinWidth(Label.USE_PREF_SIZE);
        
        LOGGER.info("Creating \"Message\" Label");
        Label messageText = new Label("");
        messageText.setId("MessageText");
        messageText.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            Alert alert = null;
            
            @Override
            public void handle(MouseEvent e) {
                LOGGER.info("Start of method");
                if(alert == null){
                    LOGGER.info("Creating \"MessageText\" Alert");
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Message Alert");
                    alert.setHeaderText("Message");
                    alert.setContentText(messageText.getText());
                }
                alert.showAndWait();
                LOGGER.info("End of Method");
            }
        });
        messageText.setTextOverrun(OverrunStyle.ELLIPSIS);
        messageText.setWrapText(true);
        
        Tooltip tp = new Tooltip("Click for popup of message");
        Tooltip.install(messageText, tp);
        
        Label messageTitle = new Label("Message: ");
        messageTitle.setMinWidth(Label.USE_PREF_SIZE);
        
        LOGGER.info("Adding items to \"statusBar\" ToolBar");
        statusBar.getItems().addAll(
            selectedTitle,
            selectedName,
            new Separator(),
            messageTitle,
            messageText);
        
        LOGGER.info("End of method");
        return statusBar;
    }
    
    public static void updateSelected(String newName){
        LOGGER.info("Start of method");
        LOGGER.info("Updating \"SelectedText\"");
        Label selectedName = (Label)statusBar.lookup("#NameOfSelectedTool");
        selectedName.setText(newName);
        LOGGER.info("End of method");
    }
    
    public static void updateMessage(String newMessage){
        LOGGER.info("Start of method");
        LOGGER.info("Updating \"MessageText\"");
        Label messageText = (Label)statusBar.lookup("#MessageText");
        messageText.setText(newMessage);
        LOGGER.info("End of method");
    }
    
    private static ToolBar createToolsBar(){
        LOGGER.info("Start of method");
        LOGGER.info("Creating \"toolsBar\" ToolBar");
        toolsBar = new ToolBar();
        toolsBar.setOrientation(Orientation.VERTICAL);
        toolsBar.setId("ToolBar");
        
        LOGGER.info("Creating \"Select\" Button");
        Button selectToolButton = new Button();
        selectToolButton.setId("Select");
        selectToolButton.setOnAction( e -> {
            LOGGER.info("Start of method");
            updateSelected("Select");
            updateMessage("Selects objects in the map");
            MapArea.setTool(new SelectTool());
            LOGGER.info("End of method");
        });
        
        LOGGER.info("Creating \"Move\" Button");
        Button moveToolButton = new Button();
        moveToolButton.setId("Move");
        moveToolButton.setOnAction( e -> {
            LOGGER.info("Start of method");
            updateSelected("Move");
            updateMessage("Currently not implemented");
            LOGGER.info("End of method");
        });
        
        LOGGER.info("Creating \"Room\" MenuButton");
        MenuButton roomMenuButton = createRoomMenuButton();
        
        LOGGER.info("Creating \"Path\" Button");
        Button pathToolButton = new Button();
        pathToolButton.setId("Path");
        pathToolButton.setOnAction( e -> {
            LOGGER.info("Start of method");
            updateSelected("Path");
            updateMessage("Currently not implemented");
            LOGGER.info("End of method");
        });
        
        LOGGER.info("Creating \"Erase\" Button");
        Button eraseToolButton = new Button();
        eraseToolButton.setId("Erase");
        eraseToolButton.setOnAction( e -> {
            LOGGER.info("Start of method");
            updateSelected("Erase");
            updateMessage("Currently not implemented");
            LOGGER.info("End of method");
        });
        
        LOGGER.info("Creating \"Door\" Button");
        Button doorToolButton = new Button();
        doorToolButton.setId("Door");
        doorToolButton.setOnAction( e -> {
            LOGGER.info("Start of method");
            updateSelected("Door");
            updateMessage("Currently not implemented");
            LOGGER.info("End of method");
        });
        
        LOGGER.info("Adding items to \"toolsBar\" ToolBar");
        toolsBar.getItems().addAll(
            selectToolButton,
            moveToolButton,
            roomMenuButton,
            pathToolButton,
            eraseToolButton,
            doorToolButton);
        
        LOGGER.info("End of method");
        return toolsBar;
    }
    
    private static MenuButton createRoomMenuButton(){
        LOGGER.info("Start of method");
        LOGGER.info("Creainng \"Room\" MenuButton");
        MenuButton roomMenuButton = new MenuButton();
        roomMenuButton.setId("Room");
        
        LOGGER.info("Creating \"Line\" MenuItem");
        MenuItem lineMenuItem = new MenuItem("Line");
        lineMenuItem.setOnAction( e -> {
            LOGGER.info("Start of method");
            updateSelected("Line Tool");
            updateMessage("Currently not implemented");
            LOGGER.info("End of method");
        });
        
        LOGGER.info("Creating \"Triangle\" MenuItem");
        MenuItem triangleMenuItem = new MenuItem("Triangle");
        triangleMenuItem.setOnAction( e -> {
            LOGGER.info("Start of method");
            updateSelected("Triangle Tool");
            updateMessage("Currently not implemented");
            MapArea.add(
                    new Polygon(
                            new double[]{
                                10.0, 10.0,
                                30.0, 10.0,
                                10.0, 30.0}));
            LOGGER.info("End of method");
        });
        
        LOGGER.info("Creating \"Rectangle\" MenuItem");
        MenuItem rectangleMenuItem = new MenuItem("Rectangle");
        rectangleMenuItem.setOnAction( e -> {
            LOGGER.info("Start of method");
            updateSelected("Rectangle Tool");
            updateMessage("Currently not implemented");
            LOGGER.info("End of method");
        });
        
        LOGGER.info("Creating \"Pentagon\" MenuItem");
        MenuItem pentagonMenuItem = new MenuItem("Pentagon");
        pentagonMenuItem.setOnAction( e -> {
            LOGGER.info("Start of method");
            updateSelected("Pengtagon Tool");
            updateMessage("Currently not implemented");
            LOGGER.info("End of method");
        });
        
        LOGGER.info("Creating \"Hexagon\" MenuItem");
        MenuItem hexagonMenuItem = new MenuItem("Hexagon");
        hexagonMenuItem.setOnAction( e -> {
            LOGGER.info("Start of method");
            updateSelected("Hexagon Tool");
            updateMessage("Currently not implemented");
            LOGGER.info("End of method");
        });
        
        roomMenuButton.getItems().addAll(
            lineMenuItem,
            triangleMenuItem,
            rectangleMenuItem,
            pentagonMenuItem,
            hexagonMenuItem
            );
        
        LOGGER.info("End of method");
        return roomMenuButton;
    }
    
    @Override
    public void initiateLogging(){
        LogData.initiateLogging(this.getClass().getSimpleName(), LOGGER);
        LOGGER.info("Logging started");
    }
}
