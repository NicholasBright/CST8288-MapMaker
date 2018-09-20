package mapmaker.tool;

import java.util.Arrays;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
        target.getChildren().add(createdRoom);
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
    public void mouseDragged(MouseEvent e) {
        /*double eX = e.getX();
        double eY = e.getY();
        
        if(eX < target.getBoundsInLocal().getMinX())
            eX = 0;
        else if (eX > target.getBoundsInLocal().getMaxX())
            eX = target.getBoundsInLocal().getWidth();
        
        if(eY < target.getBoundsInLocal().getMinY())
            eY = 0;
        else if (eY > target.getBoundsInLocal().getMaxY())
            eY = target.getBoundsInLocal().getHeight();*/
        
        Point2D newEnd = new Point2D(e.getX(), e.getY());
        Room newRoom = new Room(createdRoom.getNumSides(), createdRoom.getSideLength(), startPoint, newEnd);
        Double newPoints[] = Arrays.copyOf(newRoom.getPoints().toArray(),newRoom.getPoints().toArray().length, Double[].class);
        boolean inBoundsFlag = true;
        for(int i=0;i<newPoints.length;i++){
            double x = newPoints[i];
            double y = newPoints[++i];
            if(! target.getBoundsInLocal().contains(x, y))
                inBoundsFlag = false;
        }
        if(inBoundsFlag)
            createdRoom.updateShape(sides, newEnd.distance(startPoint), startPoint, newEnd);
    }
    
}
