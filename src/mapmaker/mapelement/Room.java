package mapmaker.mapelement;

import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

/**
 *
 * @author nick
 */
public final class Room extends Polygon{
    int numSides;
    double sideLength;
    Rotate rotation;
    ArrayList<ControlPoint> controlPoints = new ArrayList<>();
    
    public Room(int numSides, double sideLength, Point2D ... startPoints){
        rotation = new Rotate();
        rotation.setAngle(0);
        this.getTransforms().add(rotation);
        updateShape(numSides, sideLength, startPoints);
        this.getStyleClass().add("room");
    }
    
    public Room(int numSides, double sideLength, Double ... startPoints){
        rotation = new Rotate();
        rotation.setAngle(0);
        this.getTransforms().add(rotation);
        updateShape(numSides, sideLength, startPoints);
        this.getStyleClass().add("room");
    }
    
    public int getNumSides(){ return numSides;}
    public double getSideLength(){ return sideLength;}
    
    public void updateShape(int numSides, double sideLength){
        updateShape(numSides, sideLength, (Double[]) getPoints().toArray());
    }
    
    public void updateShape(int numSides, double sideLength, Double ... pointPieces) throws ArrayIndexOutOfBoundsException {
        if( pointPieces.length < 2) throw new ArrayIndexOutOfBoundsException("At least 2 doubles must be provided to form a start point");
        else if( pointPieces.length < 4)
            updateShape(numSides, sideLength, new Point2D(pointPieces[0],pointPieces[1]));
        else if( pointPieces.length >= 4)
            updateShape(numSides, sideLength, new Point2D(pointPieces[0],pointPieces[1]), new Point2D(pointPieces[2],pointPieces[3]));
    }
    
    public void updateShape(int numSides, double sideLength, Point2D ... startPoints){
        if(numSides < 2) numSides = 2;
        
        this.numSides = numSides;
        this.sideLength = sideLength;
        
        this.getPoints().clear();
        
        Double points[] = new Double[numSides*2];
        if(controlPoints.size() > 0)
            controlPoints.get(0).setPosition(startPoints[0]);
        else
            controlPoints.add(new ControlPoint(this, startPoints[0]));
        points[0] = startPoints[0].getX();
        points[1] = startPoints[0].getY();
        if(startPoints.length > 1){
            points[2] = startPoints[1].getX();
            points[3] = startPoints[1].getY();
            if(controlPoints.size() > 1)
                controlPoints.get(1).setPosition(startPoints[1]);
            else
                controlPoints.add(new ControlPoint(this, startPoints[1]));
        }
        else {
            points[2] = startPoints[0].getX() + sideLength;
            points[3] = startPoints[0].getY();
            if(controlPoints.size() > 1)
                controlPoints.get(1).setPosition(points[2], points[3]);
            else
                controlPoints.add(new ControlPoint(this, points[2], points[3]));
        }
        
        Point2D beforeLast;
        Point2D last;
        Rotate angleBetweenPoints = new Rotate(((numSides-2)*-180)/numSides);
        for(int i=4;i<numSides*2;i++){
            beforeLast = new Point2D(points[i-4], points[i-3]);
            last = new Point2D(points[i-2], points[i-1]);
            Point2D newVector = angleBetweenPoints.deltaTransform(beforeLast.getX() - last.getX(),
                    beforeLast.getY() - last.getY());
            points[i] = newVector.getX() + last.getX();
            points[i+1] = newVector.getY() + last.getY();
            if(controlPoints.size() > i/2)
                controlPoints.get(i/2).setPosition(points[i], points[++i]);
            else
                controlPoints.add(new ControlPoint(this, points[i], points[++i]));
        }
        
        super.getPoints().addAll(points);
    }
    
    public void updateShape(){
        super.getPoints().clear();
        controlPoints
            .stream()
            .forEach( (cp) -> {
                super.getPoints().addAll(cp.getCenterX(), cp.getCenterY());
            });
    }
    
    public final ArrayList<ControlPoint> getControlPoints(){
        return controlPoints;
    }
}
