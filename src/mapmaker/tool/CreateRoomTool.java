package mapmaker.tool;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mapmaker.mapelement.ControlPoint;
import mapmaker.mapelement.Room;

/**
 *
 * @author owner
 */
public final class CreateRoomTool extends Tool {
    int sides;
    Point2D startPoint;
    Room createdRoom;
    
    public CreateRoomTool (Pane target){
        super("Room Tool", "Creates rooms", target);
        //this.sides = 2;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        this.sides = (Integer) ToolState.getToolState().getOption(0);
        startPoint = new Point2D(e.getX(), e.getY());
        createdRoom = new Room(sides, 1.0, startPoint);
        target.getChildren().add(createdRoom);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

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
    
    @Override
    public void reset() {
    }
}
