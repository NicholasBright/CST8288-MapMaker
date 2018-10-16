package mapmaker.tool;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import mapmaker.MapArea;
import mapmaker.mapelement.PolyRoom;

/**
 *
 * @author owner
 */
public final class CreateRoomTool extends Tool {
    Point2D startPoint;
    PolyRoom createdRoom;
    
    public CreateRoomTool (MapArea target){
        super("Room Tool", "Creates rooms", target);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        createdRoom = new PolyRoom();
        createdRoom.setNumSides(Integer.class.cast(ToolState.getToolState().getOption(0)));
        createdRoom.setCenterX(e.getX());
        createdRoom.setCenterY(e.getY());
        createdRoom.redraw();
        target.add(createdRoom);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDragged(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        createdRoom.normalizeShape(e.getX(), e.getY());
    }
    
    @Override
    public void mouseMoved(MouseEvent e){
        
    }
    
    @Override
    public void reset() {
    }
}
