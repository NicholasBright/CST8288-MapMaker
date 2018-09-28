package mapmaker.tool;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mapmaker.MapArea;
import mapmaker.mapelement.ControlPoint;
import mapmaker.mapelement.Room;

/**
 *
 * @author owner
 */
public class MoveTool extends Tool {
    Point2D lastPoint = null;
    Group cpGroup = null;
    
    public MoveTool(Pane target){
        super("Move Tool", "Click and drag to move selected control points around the pane", target);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        lastPoint = new Point2D(e.getX(), e.getY());
        cpGroup = new Group();
        target.getChildren()
            .stream()
            .forEach( (n) -> {
                if(n instanceof Room){
                    Room r = (Room)n;
                    r.getControlPoints()
                        .stream()
                        .filter( (cp) -> (cp.isSelected()))
                        .forEach( (cp) -> {
                            cpGroup.getChildren().add(cp);
                        });
                }
            });
        target.getChildren().add(cpGroup);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        target.getChildren().remove(cpGroup);
        target.getChildren().addAll(cpGroup.getChildren());
        cpGroup.getChildren().clear();
        target = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Bounds pBounds = target.getBoundsInLocal();
        Bounds gBounds = cpGroup.getBoundsInParent();
        final double xTrans;
        final double yTrans;
        
        if(pBounds.getMaxX() < gBounds.getMaxX() + e.getX() - lastPoint.getX()){
            xTrans = pBounds.getMaxX() - gBounds.getMaxX();
        } else if (pBounds.getMinX() > gBounds.getMinX() + e.getX() - lastPoint.getX()){
            xTrans = pBounds.getMinX() - gBounds.getMinX();
        }
        else {
            xTrans = e.getX() - lastPoint.getX();
        }
        
        if(pBounds.getMaxY() < gBounds.getMaxY() + e.getY() - lastPoint.getY()){
            yTrans = pBounds.getMaxY() - gBounds.getMaxY();
        } else if (pBounds.getMinY() > gBounds.getMinY() + e.getY() - lastPoint.getY()){
            yTrans = pBounds.getMinY() - gBounds.getMinY();
        }
        else {
            yTrans = e.getY() - lastPoint.getY();
        }
        
        cpGroup.getChildren().forEach( (o) -> {
            if(o instanceof ControlPoint){
                ControlPoint cp = (ControlPoint) o;
                cp.setX(cp.getCenterX() + xTrans);
                cp.setY(cp.getCenterY() + yTrans);
            }
        });
        
        lastPoint = new Point2D(lastPoint.getX() + xTrans, e.getY());
    }
}
