package mapmaker;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
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
import mapmaker.uielements.ValidatedTextField;

public class MapMaker extends Application {
    private MapArea                 mapArea;
    private ScrollPane              centerPane;
    private Scene                   rootScene;
    private BorderPane              rootPane;
    private MenuBar                 menuBar;
    private ToolBar                 statusBar;
    private ToolBar                 toolBar;
    private VBox                    detailsBox;
    
    private SimpleStringProperty    descriptionProperty;
    private ObservableList<String>  messageList;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        descriptionProperty = new SimpleStringProperty();
        messageList = FXCollections.observableArrayList();
        messageList.add("Enjoy the map maker!");
        
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
            rootScene.getStylesheets().add( ResourceManager.get().getCSSUri("style.css"));
        }
        catch (FileNotFoundException e){
            Logger.getLogger(MapMaker.class.getSimpleName()).log(Level.SEVERE, e.toString(), e);
        }
    }
    
    public Pane buildRootPane(){
        rootPane = new BorderPane();
        
        mapArea = new MapArea();
        
        centerPane = new ScrollPane(mapArea);
        
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
                                alert.setContentText( ResourceManager.get().loadTxtToString("credits.txt") );
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
                                alert.setContentText( ResourceManager.get().loadTxtToString("info.txt") );
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
                                alert.setContentText( ResourceManager.get().loadTxtToString("help.txt") );
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
        
        Label description = createLabel(null, "DescriptionLabel", Label.USE_PREF_SIZE);
        description.textProperty().bind(descriptionProperty);
        
        statusBar = createToolBar( "StatusBar", Orientation.HORIZONTAL,
                createLabel("Selected: ", null, Label.USE_PREF_SIZE),
                selectedName,
                new Separator(),
                createLabel("Description: ", null, Label.USE_PREF_SIZE),
                description
        );
        
        //Creating the Room MenuButton before so I don't need a one off method
        MenuButton roomMenuButton = new MenuButton();
        roomMenuButton.setId("Room");
        roomMenuButton.getItems().addAll(createMenuItem("Line", e -> {
                    ToolState.getToolState().setActiveTool(new CreateRoomTool(mapArea), 2);
                    descriptionProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
            }),
            createMenuItem("Triangle", e -> {
                    ToolState.getToolState().setActiveTool(new CreateRoomTool(mapArea), 3);
                    descriptionProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
            }),
            createMenuItem("Retangle", e -> {
                    ToolState.getToolState().setActiveTool(new CreateRoomTool(mapArea), 4);
                    descriptionProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
            }),
            createMenuItem("Pentagon", e -> {
                    ToolState.getToolState().setActiveTool(new CreateRoomTool(mapArea), 5);
                    descriptionProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
            }),
            createMenuItem("Hexagon", e -> {
                    ToolState.getToolState().setActiveTool(new CreateRoomTool(mapArea), 6);
                    descriptionProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
            })
        );
        
        toolBar = createToolBar("ToolBar", Orientation.VERTICAL,
                createButton(null, "Select", e -> {
                    ToolState.getToolState().setActiveTool(new SelectTool(mapArea));
                    descriptionProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
                }),
                createButton(null, "Move", e -> {
                    ToolState.getToolState().setActiveTool(new MoveTool(mapArea));
                    descriptionProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
                }),
                createButton(null, "Path", e -> {
                    ToolState.getToolState().setActiveTool(new PathTool(mapArea));
                    descriptionProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
                }),
                roomMenuButton,
                createButton(null, "Erase", e -> {
                    ToolState.getToolState().setActiveTool(new EraseTool(mapArea));
                    descriptionProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
                }),
                createButton(null, "Door", e-> {
                    ToolState.getToolState().setActiveTool(new DoorTool(mapArea));
                    descriptionProperty.set(ToolState.getToolState().getActiveTool().getDescriptionProperty().get());
                })
        );
        
        detailsBox = new VBox();
        detailsBox.setId("DetailsBox");
        detailsBox.prefHeightProperty().bind(rootPane.heightProperty());
        detailsBox.prefWidthProperty().bind(rootPane.widthProperty().divide(5.0));
        
        ListView<Label> optionsList = new ListView<>();
        optionsList.setId("OptionsPane");
        optionsList.prefHeightProperty().bind(detailsBox.heightProperty().multiply(0.5));
        optionsList.maxWidthProperty().bind(detailsBox.widthProperty());
        optionsList.getItems().addListener((ListChangeListener.Change<? extends Label> c)->{
            while(c.next()){
                c.getAddedSubList()
                    .stream()
                    .forEach((l) -> {
                        l.prefWidthProperty().bind(optionsList.widthProperty());
                    });
            }
        });
        
        ObservableList<Label> roomList = FXCollections.observableArrayList();
        ListView<Label> roomListView = new ListView<>(roomList);
        roomListView.setId("RoomList");
        
        roomListView.prefHeightProperty().bind(detailsBox.heightProperty().multiply(0.5));
        roomListView.prefWidthProperty().bind(detailsBox.widthProperty());
        
        roomListView.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if(!e.isShiftDown()){
                mapArea.getRooms().stream().forEach((room) -> {
                    room.setSelected(false);
                });
            }
            
            Label label = roomListView.getSelectionModel().getSelectedItem();
            if(label != null){
                Room room = ((Room) label.getUserData());
                room.setSelected(true);
            }
        });
        
        roomListView.getSelectionModel().selectedItemProperty().addListener((o, oV, nV)->{
            optionsList.getItems().clear();
            if(nV != null){
                //nV.setStyle("-fx-underline: true; ");
                optionsList.setItems(buildOptionList(((Room)nV.getUserData()).getModifiablePropertiesList()));
            }
        });
        
        mapArea.getRooms().addListener((ListChangeListener.Change<? extends Room> c) -> {
            while(c.next()){
                c.getAddedSubList()
                 .stream()
                 .forEach(r -> {
                    Label newRoomLabel = new Label();
                    newRoomLabel.textProperty().bind(r.nameProperty());
                    newRoomLabel.setUserData(r);
                    r.selectedProperty().addListener((o, oV, nV) -> {
                        if(nV)
                            roomListView.getSelectionModel().select(newRoomLabel);
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
        
        detailsBox.getChildren().addAll(roomListView, optionsList);
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
    
    public ObservableList<Label> buildOptionList(ObservableList<Property> propList){
        ObservableList<Label> optList = FXCollections.observableArrayList();
        propList.stream().forEach((Property p) -> {
                Control ctrl;
                if(p instanceof BooleanProperty){
                    ComboBox<String> boolCB = new ComboBox<>();
                    BooleanProperty.class.cast(p).addListener((o, oV, nV) -> {
                        if(! nV.toString().equals(boolCB.getValue().toLowerCase())){
                            boolCB.valueProperty().set(nV ? "True" : "False");
                        }
                    });
                    boolCB.getItems().addAll("True", "False");
                    boolCB.valueProperty().set(((BooleanProperty) p).getValue() ? "True" : "False");
                    boolCB.valueProperty().addListener((o, oV, nV) -> {
                        ((BooleanProperty) p).set(Boolean.parseBoolean(nV));
                    });
                    ctrl = boolCB;
                }
                else {
                    ValidatedTextField tf;
                    if(p instanceof IntegerProperty){
                        tf = new ValidatedTextField(p.getValue().toString());
                        IntegerProperty.class.cast(p).addListener((o, oV, nV)->{
                            if(! nV.toString().equals(tf.getText()))
                                tf.setText(p.getValue().toString());
                        });
                        tf.setValidateFunction((s) -> {
                            return !(Pattern.compile("\\d+").matcher(s).matches() && Integer.parseInt(s) > 0);
                        });
                        tf.setOnKeyReleased((e) -> {
                            if(!tf.isInvalid()){
                                ((IntegerProperty) p).set(Integer.parseInt(tf.getText()));
                            }
                        });
                        tf.setInvalidTooltipText("Please enter a correct integer (no decimals or others)");
                    }
                    else if(p instanceof DoubleProperty){
                        DecimalFormat df = new DecimalFormat("#.0");
                        tf = new ValidatedTextField(df.format(p.getValue()));
                        DoubleProperty.class.cast(p).addListener((o, oV, nV)->{
                            if(! df.format(nV).equals(tf.getText()))
                                tf.setText(df.format(nV));
                        });
                        tf.setValidateFunction((s) -> {
                            return !(Pattern.compile("\\d+\\.\\d").matcher(s).matches() && Double.parseDouble(s) > 0);
                        });
                        tf.setOnKeyReleased((e) -> {
                            if(!tf.isInvalid()){
                                ((DoubleProperty) p).set(Double.parseDouble(tf.getText()));
                            }
                        });
                        tf.setInvalidTooltipText("Please ensure to use a period and exactly 1 decimal place");
                    }
                    else if(p instanceof StringProperty){
                        tf = new ValidatedTextField(p.getValue().toString());
                        StringProperty.class.cast(p).addListener((o, oV, nV)->{
                            if(! nV.equals(tf.getText()))
                                tf.setText(p.getValue().toString());
                        });
                        tf.setValidateFunction((s) -> {
                            return false;
                        });
                        tf.setOnKeyReleased((e) -> {
                            if(!tf.isInvalid()){
                                ((StringProperty) p).set(tf.getText());
                            }
                        });
                    }
                    else {
                        tf = new ValidatedTextField(p.getValue().toString());
                        tf.setDisable(true);
                    }
                    
                    ctrl = tf;
                }
                
                Label optionLabel = new Label(p.getName(), ctrl);
                optionLabel.setContentDisplay(ContentDisplay.BOTTOM);
                optList.add(optionLabel);
        });
        return optList;
    }
}
