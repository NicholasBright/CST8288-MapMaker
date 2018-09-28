package mapmaker;

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
