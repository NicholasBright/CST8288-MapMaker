/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mapmaker.mapelement.Room;
import mapmaker.tool.CreateRoomTool;
import mapmaker.tool.DoorTool;
import mapmaker.tool.EraseTool;
import mapmaker.tool.MoveTool;
import mapmaker.tool.PathTool;
import mapmaker.tool.SelectTool;
import mapmaker.tool.Tool;
import mapmaker.tool.ToolState;

/**
 *
 * @author owner
 */
public class MapMaker extends Application {
    private MapArea                 mapArea;
    private ScrollPane              centerPane;
    private Scene                   rootScene;
    private BorderPane              rootPane;
    private MenuBar                 menuBar;
    private ToolBar                 statusBar;
    private ToolBar                 toolBar;
    private VBox                    detailsBox;
    
    private SimpleStringProperty    messageProperty;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        messageProperty = new SimpleStringProperty("Enjoy the map maker!");
        
        rootScene = new Scene(buildRootPane(), 800, 600);
        primaryStage.setScene(rootScene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("CST8288 - Map Maker");
        
        loadStylesheet();
        
        ToolState.getToolState().setActiveTool(new SelectTool(mapArea));
        
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
        
        mapArea = new MapArea();
        
        centerPane = new ScrollPane(mapArea);
        /*centerPane.vminProperty().bind(null);
        centerPane.setHmin(-1000);*/
        
        menuBar = createMenuBar(
            createMenu("File",
                createMenuItem("New", (e) -> {
                    mapArea.reset();
                }),
                createMenuItem("Save", (e) -> {
                }),
                createMenuItem("Reload Style", (e) -> {
                    loadStylesheet();
                }, createLabel(null, "CSS-icon", null)),
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
        
        //Setting up the special labels that are bound
        Label selectedName = createLabel(null, "SelectedToolLabel", Label.USE_PREF_SIZE);
        ToolState.getToolState().getActiveToolProperty().addListener( (ObservableValue<? extends Tool> observable, Tool oldValue, Tool newValue) -> {
            selectedName.setText(newValue.getNameProperty().get());
        });
        
        Label message = createLabel(null, "MessageLabel", Label.USE_PREF_SIZE);
        message.textProperty().bind(messageProperty);
        
        statusBar = createToolBar( "StatusBar", Orientation.HORIZONTAL,
                createLabel("Selected: ", null, Label.USE_PREF_SIZE),
                selectedName,
                new Separator(),
                createLabel("Messages: ", null, Label.USE_PREF_SIZE),
                message
        );
        
        //Creating the Room MenuButton before so I don't need a one off method
        MenuButton roomMenuButton = new MenuButton();
        roomMenuButton.setId("Room");
        roomMenuButton.getItems().addAll(
            createMenuItem("Line", e -> {
                    ToolState.getToolState().setActiveTool(new CreateRoomTool(mapArea), 2);
                    messageProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
            }),
            createMenuItem("Triangle", e -> {
                    ToolState.getToolState().setActiveTool(new CreateRoomTool(mapArea), 3);
                    messageProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
            }),
            createMenuItem("Retangle", e -> {
                    ToolState.getToolState().setActiveTool(new CreateRoomTool(mapArea), 4);
                    messageProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
            }),
            createMenuItem("Pentagon", e -> {
                    ToolState.getToolState().setActiveTool(new CreateRoomTool(mapArea), 5);
                    messageProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
            }),
            createMenuItem("Hexagon", e -> {
                    ToolState.getToolState().setActiveTool(new CreateRoomTool(mapArea), 6);
                    messageProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
            }),
            createMenuItem("Custom Room", e -> {
                    ToolState.getToolState().setActiveTool(new CreateRoomTool(mapArea), -1);
                    messageProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
            })
        );
        
        toolBar = createToolBar( "ToolBar", Orientation.VERTICAL,
                createButton(null, "Select", e -> {
                    ToolState.getToolState().setActiveTool(new SelectTool(mapArea));
                    messageProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
                }),
                createButton(null, "Move", e -> {
                    ToolState.getToolState().setActiveTool(new MoveTool(mapArea));
                    messageProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
                }),
                createButton(null, "Path", e -> {
                    ToolState.getToolState().setActiveTool(new PathTool(mapArea));
                    messageProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
                }),
                roomMenuButton,
                createButton(null, "Erase", e -> {
                    ToolState.getToolState().setActiveTool(new EraseTool(mapArea));
                    messageProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
                }),
                createButton(null, "Door", e-> {
                    ToolState.getToolState().setActiveTool(new DoorTool(mapArea));
                    messageProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
                })
        );
        
        detailsBox = new VBox();
        detailsBox.prefHeightProperty().bind(rootPane.heightProperty());
        detailsBox.prefWidthProperty().bind(rootPane.widthProperty().divide(5.0));
        
        ObservableList<Label> roomList = FXCollections.observableArrayList();
        ListView<Label> roomListView = new ListView<>(roomList);
        roomListView.setId("RoomList");
        
        roomListView.prefHeightProperty().bind(detailsBox.heightProperty().multiply(0.5));
        roomListView.prefWidthProperty().bind(detailsBox.widthProperty());
        
        mapArea.getRooms().addListener((ListChangeListener.Change<? extends Room> c) -> {
            while(c.next()){
                c.getAddedSubList()
                 .stream()
                 .forEach(r -> {
                    Label newRoomLabel = new Label();
                    newRoomLabel.textProperty().bind(r.polyNameProperty());
                    newRoomLabel.setUserData(r);
                    newRoomLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        if(!e.isShiftDown()){
                            mapArea.getRooms().stream().forEach((room) -> {
                            room.setHighlighted(false);
                            });
                        }
                        Room room = ((Room) newRoomLabel.getUserData());
                        room.setHighlighted(true);
                    });
                    r.highlightedProperty().addListener((o, oV, nV) -> {
                        newRoomLabel.setStyle((nV ? "-fx-background-color: gold" : null));
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
        mapArea.minWidthProperty().bind(
                rootPane.widthProperty().subtract(toolBar.widthProperty().add(detailsBox.widthProperty())).divide(mapArea.getScaleX())
        );
        mapArea.minHeightProperty().bind(
                rootPane.heightProperty().subtract(menuBar.heightProperty().add(statusBar.heightProperty())).divide(mapArea.getScaleY())
        );
        
        rootPane.setTop(menuBar);
        rootPane.setBottom(statusBar);
        rootPane.setLeft(toolBar);
        rootPane.setRight(detailsBox);
        rootPane.setCenter(centerPane);
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
        MenuItem mi = new MenuItem(name, createLabel(null,name+"-icon", null));
        mi.setOnAction(handler);
        return mi;
    }
    
    public MenuItem createMenuItem(String name, EventHandler<ActionEvent> handler, Label iconLabel){
        MenuItem mi = new MenuItem(name, iconLabel);
        mi.setOnAction(handler);
        return mi;
    }
    
    public Label createLabel(String text, String id, Double minWidth){
        Label l = new Label();
        l.setId(id);
        l.setText(text);
        l.setMinWidth(minWidth == null ? 0.0: minWidth);
        return l;
    }
    
    public ToolBar createToolBar(String id, Orientation orientation, Node ... items){
        ToolBar tb = new ToolBar(items);
        tb.setId(id);
        tb.setOrientation(orientation);
        return tb;
    }
    
    public Button createButton(String name, String id, EventHandler<ActionEvent> handler){
        Button b = new Button(name);
        b.setId(id);
        b.setOnAction(handler);
        return b;
    }
}
