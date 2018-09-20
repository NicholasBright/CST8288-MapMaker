package mapmaker.mapelement;

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
    
    public Room(int numSides, double sideLength, Point2D ... startPoints){
        rotation = new Rotate();
        rotation.setAngle(0);
        this.getTransforms().add(rotation);
        updateShape(numSides, sideLength, startPoints);
    }
    
    public Room(int numSides, double sideLength, Double ... startPoints){
        rotation = new Rotate();
        rotation.setAngle(0);
        this.getTransforms().add(rotation);
        updateShape(numSides, sideLength, startPoints);
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
        if(numSides < 3) numSides = 3;
        
        this.numSides = numSides;
        this.sideLength = sideLength;
        
        this.getPoints().clear();
        
        Double points[] = new Double[numSides*2];
        points[0] = startPoints[0].getX();
        points[1] = startPoints[0].getY();
        if(startPoints.length > 1){
            points[2] = startPoints[1].getX();
            points[3] = startPoints[1].getY();
        }
        else {
            points[2] = startPoints[0].getX() + sideLength;
            points[3] = startPoints[0].getY();
        }
        
        Point2D beforeLast;
        Point2D last;
        Rotate angleBetweenPoints = new Rotate(((numSides-2)*-180)/numSides);
        for(int i=4;i<numSides*2;i++){
            beforeLast = new Point2D(points[i-4], points[i-3]);
            last = new Point2D(points[i-2], points[i-1]);
            Point2D newVector = angleBetweenPoints.deltaTransform(beforeLast.getX() - last.getX(),
                    beforeLast.getY() - last.getY());
            points[i++] = newVector.getX() + last.getX();
            points[i] = newVector.getY() + last.getY();
        }
        
        super.getPoints().addAll(points);
    }
}
