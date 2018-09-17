package mapmaker;

import java.util.logging.Level;
import java.util.logging.Logger;
import mapmaker.tool.Tool;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

/**
 *
 * @author owner
 */
public class MapArea {
    private static Pane pane;// = new Pane();
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
    
    public static void add(Shape n){
        pane.getChildren().add(n);
    }
}
