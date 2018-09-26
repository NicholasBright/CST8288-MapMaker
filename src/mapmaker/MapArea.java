package mapmaker;

import java.util.ArrayList;
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
public class MapArea {
    private static final ToolState TS = ToolState.getToolState();
    
    private static Pane pane;
    private static ObservableList<Room> rooms = FXCollections.observableArrayList();
    
    public static Pane initPane(){
        pane = new Pane();
        pane.setId("MapArea");
        
        pane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            //if(TS.getActiveTool() != null)
            TS.getActiveTool().mouseClicked(e);
        });
        pane.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            //if(TS.getActiveTool() != null)
            TS.getActiveTool().mousePressed(e);
        });
        pane.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            //if(TS.getActiveTool() != null)
            TS.getActiveTool().mouseReleased(e);
        });
        pane.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            //if(TS.getActiveTool() != null)
            TS.getActiveTool().mouseDragged(e);
        });
        return pane;
    }
    
    public static Pane getPane(){
        return pane;
    }
    
    public static void add(Room n){
        pane.getChildren().add(0, n);
        pane.getChildren().addAll(n.getControlPoints());
        rooms.add(n);
    }
    
    public static final ObservableList<Room> getRooms(){
        return rooms;
    }
    
    public static void reset(){
        pane.getChildren().clear();
        rooms.clear();
    }
    
    public static void remove(Room r){
        pane.getChildren().remove(r);
        pane.getChildren().removeAll(r.getControlPoints());
        rooms.remove(r);
    }
    
    public static void removeAll(List<Room> l){
        l.stream()
         .forEach( (r) -> {
             remove(r);
         });
    }
}
