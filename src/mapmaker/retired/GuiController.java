/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.retired;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import mapmaker.MapArea;
import mapmaker.ResourceLoader;
import mapmaker.mapelement.Room;
import mapmaker.tool.CreateRoomTool;
import mapmaker.tool.EraseTool;
import mapmaker.tool.MoveTool;
import mapmaker.tool.SelectTool;
import mapmaker.tool.Tool;
import mapmaker.tool.ToolState;

/**
 *
 * @author owner
 */
class GuiController{
    private static BorderPane rootPane;
    private static MenuBar    menuBar;
    private static ToolBar    statusBar;
    private static ToolBar    toolsBar;
    private static VBox       detailsBox;
    
    public static BorderPane createRootPane(){
        rootPane = new BorderPane();
        
        rootPane.setTop(createMenuBar());
        rootPane.setBottom(createStatusBar());
        rootPane.setLeft(createToolsBar());
        rootPane.setRight(createDetailsBox());
        
        rootPane.setCenter(MapArea.initPane());
        
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
        newMenuItem.setOnAction(e -> {
            MapArea.reset();
        });
        newMenuItem.setId("New");
        
        Label saveLabel = new Label();
        saveLabel.setId("Save-icon");
        MenuItem saveMenuItem = new MenuItem("Save", saveLabel);
        saveMenuItem.setDisable(true);
        saveMenuItem.setId("Save");
        
        Label reloadStyleLabel = new Label();
        reloadStyleLabel.setId("CSS-icon");
        MenuItem reloadStyleMenuItem = new MenuItem("Reload Style", reloadStyleLabel);
        reloadStyleMenuItem.setOnAction(e -> {
            //MapMaker.loadStylesheet();
        });
        
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
            reloadStyleMenuItem,
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
        creditMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            Alert alert = null;
            
            @Override
            public void handle(ActionEvent e) {
                if(alert == null){
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Credits");
                    alert.setHeaderText("Credits");
                    try {
                        alert.setContentText( ResourceLoader.loadTxtToString("credits.txt") );
                    } catch (FileNotFoundException ex) {
                        alert.setContentText( "Credits file not found. Report error to developer." );
                        alert.showAndWait();
                        alert = null;
                        return;
                    }
                }
                alert.showAndWait();
            }
        });
        
        Label infoLabel = new Label();
        infoLabel.setId("Info-icon");
        MenuItem infoMenuItem = new MenuItem("Info", infoLabel);
        infoMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            Alert alert = null;
            @Override
            public void handle(ActionEvent e) {
                if(alert == null){
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Info");
                    alert.setHeaderText("Info");
                    try {
                        alert.setContentText( ResourceLoader.loadTxtToString("info.txt") );
                    } catch (FileNotFoundException ex) {
                        alert.setContentText( "Info file not found. Report error to developer." );
                        alert.showAndWait();
                        alert = null;
                        return;
                    }
                }
                alert.showAndWait();
            }
        });
        
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        
        Label helpLabel = new Label();
        helpLabel.setId("Help-icon");
        MenuItem helpMenuItem = new MenuItem("Help", helpLabel);
        helpMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            Alert alert = null;
            @Override
            public void handle(ActionEvent e) {
                if(alert == null){
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Help");
                    alert.setHeaderText("Help");
                    try {
                        alert.setContentText( ResourceLoader.loadTxtToString("help.txt") );
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(GuiController.class.getSimpleName()).log(Level.SEVERE, ex.toString(), ex);
                        alert.setContentText( "Help file not found. Report error to developer." );
                        alert.showAndWait();
                        alert = null;
                        return;
                    }
                }
                alert.showAndWait();
            }
        });
        
        helpMenu.getItems().addAll(
            creditMenuItem,
            infoMenuItem,
            separatorMenuItem, 
            helpMenuItem);
        
        return helpMenu;
    }
    
    private static ToolBar createStatusBar(){
        statusBar = new ToolBar();
        statusBar.setId("StatusBar");
        
        Label selectedTitle = new Label("Selected: ");
        selectedTitle.setMinWidth(Label.USE_PREF_SIZE);
        Label selectedName = new Label();
        ToolState.getToolState().getActiveToolProperty().addListener( (ObservableValue<? extends Tool> observable, Tool oldValue, Tool newValue) -> {
            selectedName.setText(newValue.getNameProperty().get());
        });
        //selectedName.setText(ToolState.getToolState().getActiveToolProperty().get().getNameProperty().get());
        //selectedName.setId("NameOfSelectedTool");
        selectedName.setMinWidth(Label.USE_PREF_SIZE);
        
        Label messageText = new Label("");
        messageText.setId("MessageText");
        messageText.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            Alert alert = null;
            
            @Override
            public void handle(MouseEvent e) {
                if(alert == null){
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Message Alert");
                    alert.setHeaderText("Message");
                    alert.setContentText(messageText.getText());
                }
                alert.showAndWait();
            }
        });
        messageText.setTextOverrun(OverrunStyle.ELLIPSIS);
        messageText.setWrapText(true);
        
        Tooltip tp = new Tooltip("Click for popup of message");
        Tooltip.install(messageText, tp);
        
        Label messageTitle = new Label("Message: ");
        messageTitle.setMinWidth(Label.USE_PREF_SIZE);
        
        statusBar.getItems().addAll(
            selectedTitle,
            selectedName,
            new Separator(),
            messageTitle,
            messageText);
        
        return statusBar;
    }
    
    private static ToolBar createToolsBar(){
        toolsBar = new ToolBar();
        toolsBar.setOrientation(Orientation.VERTICAL);
        toolsBar.setId("ToolBar");
        
        Button selectToolButton = new Button();
        selectToolButton.setId("Select");
        selectToolButton.setOnAction( e -> {
            ToolState.getToolState().setActiveTool(SelectTool.getTool());
        });
        
        Button moveToolButton = new Button();
        moveToolButton.setId("Move");
        moveToolButton.setOnAction( e -> {
            ToolState.getToolState().setActiveTool(MoveTool.getTool());
        });
        
        MenuButton roomMenuButton = createRoomMenuButton();
        
        Button pathToolButton = new Button();
        pathToolButton.setId("Path");
        pathToolButton.setOnAction( e -> {
        });
        
        Button eraseToolButton = new Button();
        eraseToolButton.setId("Erase");
        eraseToolButton.setOnAction( e -> {
            ToolState.getToolState().setActiveTool(EraseTool.getTool());
        });
        
        Button doorToolButton = new Button();
        doorToolButton.setId("Door");
        doorToolButton.setOnAction( e -> {
        });
        
        toolsBar.getItems().addAll(
            selectToolButton,
            moveToolButton,
            roomMenuButton,
            pathToolButton,
            eraseToolButton,
            doorToolButton);
        
        return toolsBar;
    }
    
    private static MenuButton createRoomMenuButton(){
        MenuButton roomMenuButton = new MenuButton();
        roomMenuButton.setId("Room");
        
        MenuItem lineMenuItem = new MenuItem("Line");
        lineMenuItem.setOnAction( e -> {
            ToolState.getToolState().setActiveTool(CreateRoomTool.getTool(), 2);
        });
        
        MenuItem triangleMenuItem = new MenuItem("Triangle");
        triangleMenuItem.setOnAction( e -> {
            ToolState.getToolState().setActiveTool(CreateRoomTool.getTool(), 3);
        });
        
        MenuItem rectangleMenuItem = new MenuItem("Rectangle");
        rectangleMenuItem.setOnAction( e -> {
            ToolState.getToolState().setActiveTool(CreateRoomTool.getTool(), 4);
        });
        
        MenuItem pentagonMenuItem = new MenuItem("Pentagon");
        pentagonMenuItem.setOnAction( e -> {
            ToolState.getToolState().setActiveTool(CreateRoomTool.getTool(), 5);
        });
        
        MenuItem hexagonMenuItem = new MenuItem("Hexagon");
        hexagonMenuItem.setOnAction( e -> {
            ToolState.getToolState().setActiveTool(CreateRoomTool.getTool(), 6);
        });
        
        roomMenuButton.getItems().addAll(
            lineMenuItem,
            triangleMenuItem,
            rectangleMenuItem,
            pentagonMenuItem,
            hexagonMenuItem
            );
        
        return roomMenuButton;
    }
    
    private static VBox createDetailsBox(){
        detailsBox = new VBox();
        detailsBox.prefHeightProperty().bind(rootPane.heightProperty());
        detailsBox.prefWidthProperty().bind(rootPane.widthProperty().divide(5.0));
        
        ObservableList<Label> roomList = FXCollections.observableArrayList();
        ListView<Label> roomListView = new ListView<Label>(roomList);
        roomListView.setId("RoomList");
        
        roomListView.prefHeightProperty().bind(detailsBox.heightProperty().multiply(0.5));
        roomListView.prefWidthProperty().bind(detailsBox.widthProperty());
        
        MapArea.getRooms().addListener((Change<? extends Room> c) -> {
            while(c.next()){
                c.getAddedSubList()
                 .stream()
                 .forEach( r -> {
                    String polyName;
                    switch(r.getNumSides()){
                        case 2:
                            polyName = "Line";
                            break;
                        case 3: 
                            polyName = "Triangle";
                            break;
                        case 4:
                            polyName = "Rectangle";
                            break;
                        case 5:
                            polyName = "Pentagon";
                            break;
                        case 6:
                            polyName = "Hexagon";
                            break;
                        default:
                            polyName = "Polygon";
                            break;
                    }
                    Label newRoomLabel = new Label(polyName);
                    newRoomLabel.setUserData(r);
                    newRoomLabel.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                        ((Room) newRoomLabel.getUserData()).setHighlighted(true);
                    });
                    newRoomLabel.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                        ((Room) newRoomLabel.getUserData()).setHighlighted(false);
                    });
                    roomList.add(newRoomLabel);
                });
                ArrayList<Label> toDel = new ArrayList<>();
                c.getRemoved()
                 .stream()
                 .forEach( r -> {
                    roomList.stream()
                        .filter( (l) -> (l instanceof Label))
                        .forEach( l -> {
                            Label label = (Label) l;
                            if(label.getUserData() == r){
                                toDel.add(l);
                            }
                        });
                });
                roomList.removeAll(toDel);
            }
        });
        
        GridPane detailsPane = new GridPane();
        detailsPane.setId("Details");
        detailsPane.prefHeightProperty().bind(detailsBox.heightProperty().multiply(0.5));
        detailsPane.prefWidthProperty().bind(detailsBox.widthProperty());
        
        detailsBox.getChildren().addAll(roomListView, detailsPane);
        
        return detailsBox;
    }
}
