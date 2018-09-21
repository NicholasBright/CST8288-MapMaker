package mapmaker;

import java.util.ArrayList;
import mapmaker.tool.Tool;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mapmaker.mapelement.Room;

/**
 *
 * @author owner
 */
public class MapArea {
    private static Pane pane;
    private static ArrayList<Room> rooms = new ArrayList<>();
    private static Tool tool;
    
    public static Pane initPane(){
        pane = new Pane();
        pane.setId("MapArea");
        
        pane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if(tool != null)
                tool.mouseClicked(e);
        });
        pane.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if(tool != null)
                tool.mousePressed(e);
        });
        pane.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            if(tool != null)
                tool.mouseReleased(e);
        });
        pane.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if(tool != null)
                tool.mouseDragged(e);
        });
        return pane;
    }
    
    public static Pane getPane(){
        return pane;
    }
    
    public static void setTool(Tool t){
        tool = t;
        tool.setTarget(pane);
    }
    
    public static final Tool getTool(){
        return tool;
    }
    
    public static void add(Room n){
        pane.getChildren().add(0, n);
        pane.getChildren().addAll(n.getControlPoints());
        rooms.add(n);
    }
    
    public static final ArrayList<Room> getRooms(){
        return rooms;
    }
    
    public static void reset(){
        pane.getChildren().clear();
        rooms.clear();
    }
}
