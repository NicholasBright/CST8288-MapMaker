package mapmaker.tool;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import mapmaker.MapArea;
import mapmaker.mapelement.Room;

/**
 *
 * @author owner
 */
public final class CreateRoomTool extends Tool {
    Point2D startPoint;
    Room createdRoom;
    boolean irregularFlag = false;
    
    public CreateRoomTool (MapArea target){
        super("Room Tool", "Creates rooms", target);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        int numSides = Integer.class.cast(ToolState.getToolState().getOption(0));
        
        if(numSides > 0){
            irregularFlag = false;
            createdRoom = new Room(e.getX(), e.getY(), 0.0, numSides);
            target.getScene().setCursor(Cursor.CLOSED_HAND);
        } else if (createdRoom == null){
            irregularFlag = true;
            createdRoom = new Room(e.getX(), e.getY(), 0.0, 0);
            createdRoom.setRegular(false);
        }
        
        target.add(createdRoom);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(irregularFlag){
            createdRoom.addControlPoint(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(irregularFlag){
        } else {
            mouseDragged(e);
            target.checkBounds();
            target.getScene().setCursor(Cursor.DEFAULT);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(!irregularFlag)
            createdRoom.normalizeShape(e.getX(), e.getY());
    }
    
    @Override
    public void mouseMoved(MouseEvent e){
        
    }
    
    @Override
    public void reset() {
    }
}
