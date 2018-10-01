/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.mapelement;

import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

/**
 *
 * @author nick
 */
public final class Room extends Parent {
    private static PseudoClass HIGHLIGHTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("highlighted");
    
    int numSides;
    double sideLength;
    ObservableList<ControlPoint> controlPoints = FXCollections.observableArrayList();
    RoomShape internalShape = new RoomShape();
    
    private class RoomShape extends Polygon{
        private final BooleanProperty highlighted = new SimpleBooleanProperty(false) {
            @Override
            public void invalidated() {
                pseudoClassStateChanged(HIGHLIGHTED_PSEUDO_CLASS, get());
            }

            @Override
            public Object getBean() {
                return internalShape;
            }

            @Override
            public String getName() {
                return "highlighted";
            }
        };
        
        public RoomShape(){
            super();
            getStyleClass().add("room");
        }
    
        public boolean isHighlighted(){
            return this.highlighted.get();
        }

        public void setHighlighted(boolean highlighted){
            this.highlighted.set(highlighted);
        }
    }
    
    public Room(Point2D startPoint){
        this(startPoint.getX(), startPoint.getY());
    }
    
    public Room(Double x, Double y){
        internalShape = new RoomShape();
        internalShape.getPoints().addAll(x, y);
    }
    
    public Room(int numSides, double sideLength, Double startX, Double startY){
        this(numSides, sideLength, new Point2D(startX, startY));
    }
    
    public Room(int numSides, double sideLength, Point2D startPoint){
        this(numSides, sideLength, startPoint, new Point2D(startPoint.getX() + sideLength, startPoint.getY()));
    }
    
    public Room(int numSides, Point2D startPoint, Point2D firstSideEndPoint){
        this(numSides, firstSideEndPoint.distance(startPoint), startPoint, firstSideEndPoint);
    }
    
    public Room(int numSides, double sideLength, Point2D startPoint, Point2D firstSideEndPoint){
        setShape(numSides, sideLength, startPoint, firstSideEndPoint);
    }
    
    public final void setShape(Point2D startPoint, Point2D endPoint) throws IllegalArgumentException{
        setShape(getNumSides(), endPoint.distance(startPoint), startPoint, endPoint);
    }
    
    public final void setShape(Point2D startPoint) throws IllegalArgumentException{
        setShape(getNumSides(), getSideLength(), startPoint);
    }
    
    public final void setShape(int numSides, double sideLength, Point2D startPoint) throws IllegalArgumentException{
        this.setShape(numSides, sideLength, startPoint, new Point2D(startPoint.getX()+sideLength, startPoint.getY()));
    }
    
    public final void setShape(int numSides, double sideLength, Point2D startPoint, Point2D endPoint) throws IllegalArgumentException{
        if(numSides < 0)
            throw new IllegalArgumentException("numSides cannot be less than 0");
        if(sideLength < 0)
            throw new IllegalArgumentException("sideLength cannot be less than 0");
        if(startPoint == null)
            throw new IllegalArgumentException("startPoint cannot be null");
        if(endPoint == null)
            throw new IllegalArgumentException("endPoint cannot be null");
        
        this.numSides = numSides;
        this.sideLength = sideLength;
        getChildren().clear();
        internalShape.getPoints().clear();
        controlPoints.clear();
        
        Double points[] = new Double[numSides*2];
        points[0] = startPoint.getX();
        points[1] = startPoint.getY();
        controlPoints.add(new ControlPoint(this, startPoint));
        points[2] = endPoint.getX();
        points[3] = endPoint.getY();
        controlPoints.add(new ControlPoint(this, endPoint));
        
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
            controlPoints.add(new ControlPoint(this, points[i], points[++i]));
        }
        
        internalShape.getPoints().addAll(points);
        getChildren().add(internalShape);
        getChildren().addAll(controlPoints);
    }
    
    public List<ControlPoint> getControlPoints(){
        return controlPoints;
    }
    
    public int getNumSides(){
        return numSides;
    }
    
    public double getSideLength(){
        return sideLength;
    }
    
    public boolean isHighlighted(){
        return internalShape.isHighlighted();
    }
    
    public void setHighlighted(boolean highlighted){
        internalShape.setHighlighted(highlighted);
    }
    
    public void fixToPoints() {
        internalShape.getPoints().clear();
        controlPoints
            .stream()
            .forEach( (cp) -> {
                internalShape.getPoints().addAll(cp.getCenterX(), cp.getCenterY());
            });
    }
    
    public void addControlPoint(ControlPoint cp) {
        if(!getChildren().contains(cp))
            getChildren().add(cp);
        if(!controlPoints.contains(cp)){
            controlPoints.add(cp);
            ObservableList<Double> oldPoints = internalShape.getPoints();
            oldPoints.addAll(cp.getCenterX(), cp.getCenterY());
            this.getChildren().remove(internalShape);
            internalShape = new RoomShape();
            internalShape.getPoints().addAll(oldPoints);
            this.getChildren().add(0,internalShape);
        }
    }
    
    public void translate(double xTrans, double yTrans){
        int i=0;
        while(i+1 < internalShape.getPoints().size()){
            internalShape.getPoints().set(i, internalShape.getPoints().get(i) + xTrans);
            i++;
            internalShape.getPoints().set(i, internalShape.getPoints().get(i) + yTrans);
            ControlPoint cp = controlPoints.get(i/2);
            cp.setCenterX(cp.getCenterX() + xTrans);
            cp.setCenterY(cp.getCenterY() + yTrans);
        }
    }
}
