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
    
    private final ObservableList<Room> rooms = FXCollections.observableArrayList();
    
    public MapArea(){
        super();
        setId("MapArea");
        
        addEventHandler(MouseEvent.ANY, e -> {
            ToolState.getToolState().getActiveTool().handleMouseEvent(e);
        });
        
        rooms.addListener((ListChangeListener.Change<? extends Node> c) -> {
            while(c.next()){
                if(c.wasAdded()){
                    c.getAddedSubList()
                        .stream()
                        .forEach((Node n) -> {
                            if(!super.getChildren().contains(n))
                                super.getChildren().add(n);
                        });
                }
                else if(c.wasRemoved()){
                    c.getRemoved()
                        .stream()
                        .forEach((n) -> {
                            if(super.getChildren().contains(n))
                                super.getChildren().remove(n);
                        });
                }
            }
        });
        
        super.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
            while(c.next()){
                if(c.wasAdded()){
                    c.getAddedSubList()
                        .stream()
                        .forEach((Node n) -> {
                            if(n instanceof Room)
                                if(!rooms.contains((Room)n))
                                    add((Room)n);
                        });
                }
                else if(c.wasRemoved()){
                    c.getRemoved()
                        .stream()
                        .forEach((n) -> {
                            if(n instanceof Room)
                                if(rooms.contains((Room)n))
                                    remove((Room)n);
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
