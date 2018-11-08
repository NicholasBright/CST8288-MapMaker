package mapmaker.mapelement;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Line;

/**
 *
 * @author owner
 */
public class Path extends Line implements RemovableElement, TranslatableElement {
    private final Room start, end;
    private final EndPoint startPoint, endPoint;
    
    public Path(Room start, Room end){
        getStyleClass().add("path");
        
        this.start = start;
        this.end = end;
        
        startPoint = new EndPoint(this);
        endPoint = new EndPoint(this);
        
        start.radiusProperty().addListener((o, oV, nV)->{updateLine();});
        start.centerXProperty().addListener((o, oV, nV)->{updateLine();});
        start.centerYProperty().addListener((o, oV, nV)->{updateLine();});
        end.radiusProperty().addListener((o, oV, nV)->{updateLine();});
        end.centerXProperty().addListener((o, oV, nV)->{updateLine();});
        end.centerYProperty().addListener((o, oV, nV)->{updateLine();});
        
        startXProperty().bind(startPoint.centerXProperty());
        startYProperty().bind(startPoint.centerYProperty());
        endXProperty().bind(endPoint.centerXProperty());
        endYProperty().bind(endPoint.centerYProperty());
        
        updateLine();
    }
    
    private void updateLine(){
        Point2D vector = new Point2D(
            start.getCenterX() - end.getCenterX(), 
            start.getCenterY() - end.getCenterY());
        vector = vector.normalize().multiply(start.getRadius()*0.4);
        startPoint.setCenterX(start.getCenterX()-vector.getX());
        startPoint.setCenterY(start.getCenterY()-vector.getY());
        vector = vector.normalize().multiply(end.getRadius()*0.4);
        endPoint.setCenterX(end.getCenterX()+vector.getX());
        endPoint.setCenterY(end.getCenterY()+vector.getY());
    }
    
    public Room getStart(){
        return start;
    }
    
    public Room getEnd(){
        return end;
    }
    
    public EndPoint getStartPoint(){
        return startPoint;
    }
    
    public EndPoint getEndPoint(){
        return endPoint;
    }
    
    @Override
    public List<Node> remove(){
        List<Node> l = new ArrayList<>();
        l.add(this);
        l.add(startPoint);
        l.add(endPoint);
        return l;
    }
    
    @Override
    public void translate(double x, double y) {
        start.translate(x, y);
        end.translate(x, y);
    }
}
