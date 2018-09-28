package mapmaker;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
        
        addEventHandler(MouseEvent.ANY, e -> {
            ToolState.getToolState().getActiveTool().mouseClicked(e);
        });
    }
    
    public void add(Room n){
        getChildren().add(0, n);
        getChildren().addAll(n.getControlPoints());
        rooms.add(n);
    }
    
    
    public ObservableList<Room> getRooms(){
        return rooms;
    }
    
    public void reset(){
        getChildren().clear();
        rooms.clear();
    }
    
    /*
    public void remove(Room r){
        getChildren().remove(r);
        getChildren().removeAll(r.getControlPoints());
        rooms.remove(r);
    }
    
    public void removeAll(List<Room> l){
        l.stream()
         .forEach( (r) -> {
             remove(r);
         });
    }*/
}
