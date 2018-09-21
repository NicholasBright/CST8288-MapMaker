package mapmaker.tool;

import java.util.Arrays;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mapmaker.MapArea;
import mapmaker.mapelement.ControlPoint;
import mapmaker.mapelement.Room;

/**
 *
 * @author owner
 */
public class CreateRoomTool extends Tool {
    int sides;
    Point2D startPoint;
    Room createdRoom;
    
    public CreateRoomTool (int sides, Pane target, String toolName){
        super(target, toolName);
        this.sides = sides;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        startPoint = new Point2D(e.getX(), e.getY());
        createdRoom = new Room(sides, 1.0, startPoint);
        MapArea.add(createdRoom);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        return;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDragged(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {Point2D newEnd = new Point2D(e.getX(), e.getY());
        Room newRoom = new Room(createdRoom.getNumSides(), createdRoom.getSideLength(), startPoint, newEnd);
        boolean inBoundsFlag = true;
        for(ControlPoint cp : newRoom.getControlPoints()){
            if(! target.getBoundsInLocal().contains(cp.getBoundsInParent()))
                inBoundsFlag = false;
        }
        if(inBoundsFlag)
            createdRoom.updateShape(sides, newEnd.distance(startPoint), startPoint, newEnd);
    }
    
}
