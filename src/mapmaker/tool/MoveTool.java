package mapmaker.tool;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import mapmaker.MapArea;
import mapmaker.mapelement.ControlPoint;

/**
 *
 * @author owner
 */
public class MoveTool extends Tool {
    private static final MoveTool MT = new MoveTool();
    
    Point2D lastPoint = null;
    Group cpGroup = null;
    
    public MoveTool(){
        super("Move Tool", "Click and drag to move selected control points around the pane");
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        lastPoint = new Point2D(e.getX(), e.getY());
        cpGroup = new Group();
        MapArea.getRooms()
            .stream()
            .forEach( (r) -> {
                r.getControlPoints()
                    .stream()
                    .filter( (cp) -> (cp.isSelected()))
                    .forEach( (cp) -> {
                        cpGroup.getChildren().add(cp);
                    });
            });
        MapArea.getPane().getChildren().add(cpGroup);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        MapArea.getPane().getChildren().remove(cpGroup);
        MapArea.getPane().getChildren().addAll(cpGroup.getChildren());
       cpGroup.getChildren().clear();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Bounds pBounds = MapArea.getPane().getBoundsInLocal();
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
