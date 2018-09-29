package mapmaker;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mapmaker.mapelement.Room;
import mapmaker.tool.ToolState;

/**
 *
 * @author owner
 */
public class MapArea extends Pane {
    
    private static ObservableList<Room> rooms = FXCollections.observableArrayList();
    
    public MapArea(){
        setId("MapArea");
        
        addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            ToolState.getToolState().getActiveTool().mousePressed(e);
        });
        
        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            ToolState.getToolState().getActiveTool().mouseClicked(e);
        });
        
        addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            ToolState.getToolState().getActiveTool().mouseReleased(e);
        });
        
        addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            ToolState.getToolState().getActiveTool().mouseDragged(e);
        });
        
        super.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
            while(c.next()){
                if(c.wasAdded()){
                    c.getAddedSubList()
                        .stream()
                        .forEach((Node n) -> {
                            if(n instanceof Room){
                                Room r = (Room)n;
                                add(r);
                            }
                        });
                }
                else if(c.wasRemoved()){
                    c.getRemoved()
                        .stream()
                        .forEach((n) -> {
                            if(n instanceof Room){
                                Room r = (Room)n;
                                remove(r);
                            }
                        });
                }
            }
        });
    }
    
    
    public ObservableList<Room> getRooms(){
        return rooms;
    }
    
    public void reset(){
        getChildren().clear();
        rooms.clear();
    }
    
    public void add(Room r){
        rooms.add(r);
    }
    
    public void remove(Room r){
        rooms.remove(r);
    }
    
    public void removeAll(List<Room> l){
        l.stream()
         .forEach( (r) -> {
             remove(r);
         });
    }
}
