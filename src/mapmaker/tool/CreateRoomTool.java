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
    Boolean customFlag = false;
    
    public CreateRoomTool (Pane target){
        super("Room Tool", "Creates rooms", target);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        Object option = ToolState.getToolState().getOption(0);
        if(option instanceof Integer)
            sides = (Integer) option;
        if(sides > 0){
            this.sides = (Integer) ToolState.getToolState().getOption(0);
            startPoint = new Point2D(e.getX(), e.getY());
            createdRoom = new Room(sides, 1.0, startPoint);
            target.getChildren().add(createdRoom);
        }
        else if(sides < 0 && customFlag == false){
            customFlag = true;
            createdRoom = new Room(e.getX(), e.getY());
            target.getChildren().add(createdRoom);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(customFlag){
            createdRoom.addControlPoint(new ControlPoint(createdRoom, e.getX(), e.getY()));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(customFlag)
            return;
        mouseDragged(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(customFlag)
            return;
        Point2D newEnd = new Point2D(e.getX(), e.getY());
        if((new Room(sides, startPoint, newEnd)).getBoundsInLocal().getMinX() > 0 &&
           (new Room(sides, startPoint, newEnd)).getBoundsInLocal().getMinY() > 0    )
            createdRoom.setShape(startPoint, newEnd);
    }
    
    @Override
    public void reset() {
    }
}
