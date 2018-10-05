/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.mapelement;

import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import javafx.util.Pair;
import mapmaker.ModifiableOptions;

/**
 *
 * @author nick
 */
public final class Room extends Parent implements ModifiableOptions {
    private static PseudoClass HIGHLIGHTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("highlighted");
    
    double sideLength;
    ObservableList<ControlPoint> controlPoints = FXCollections.observableArrayList();
    RoomShape internalShape = new RoomShape();
    SimpleStringProperty polygonNameProperty = new SimpleStringProperty();
    
    private final BooleanProperty highlightedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty regularProperty = new SimpleBooleanProperty(true);
    private final IntegerProperty numSidesProperty = new SimpleIntegerProperty();

    @Override
    public ObservableList<Pair<String, Node>> getModifiableOptionList() {
        ObservableList<Pair<String,Node>> optionList = FXCollections.observableArrayList();
        
        ComboBox<String> regularCB = new ComboBox<String>(FXCollections.observableArrayList("True","False"));
        if(regularProperty().get())
            regularCB.getSelectionModel().select(0);
        else
            regularCB.getSelectionModel().select(1);
        
        regularCB.setOnAction( (e) -> {
            regularProperty().set(regularCB.getValue().equals("True"));
        });
        
        Spinner<Integer> sidesSpinner = new Spinner<Integer>();
        sidesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10,numSidesProperty().get()));
        sidesSpinner.valueProperty().addListener((o, oV, nV) -> {
            setShape(nV,sideLength,
                    new Point2D(controlPoints.get(0).getCenterX(),
                                controlPoints.get(0).getCenterY()),
                    new Point2D(controlPoints.get(1).getCenterX(),
                                controlPoints.get(1).getCenterY()));
        });
        
        optionList.addAll(new Pair<String,Node>("Regular",regularCB),
                          new Pair<String,Node>("Num Sides",sidesSpinner));
        
        return optionList;
    }
    
    private class RoomShape extends Polygon{
        
        public final BooleanProperty highlighted = new SimpleBooleanProperty(false) {
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
    }
    
    public Room(Point2D startPoint){
        this(startPoint.getX(), startPoint.getY());
    }
    
    public Room(Double x, Double y){
        this(0, 0, x, y);
    }
    
    public Room(int numSides, double sideLength, Double startX, Double startY){
        this(numSides, sideLength, new Point2D(startX, startY));
    }
    
    public Room(int numSides, double sideLength, Point2D startPoint){
        this(numSides, sideLength, startPoint, null);
    }
    
    public Room(int numSides, Point2D startPoint, Point2D firstSideEndPoint){
        this(numSides, firstSideEndPoint.distance(startPoint), startPoint, firstSideEndPoint);
    }
    
    public Room(int numSides, double sideLength, Point2D startPoint, Point2D firstSideEndPoint){
        setShape(numSides, sideLength, startPoint, firstSideEndPoint);
        addListenerForCPList();
        internalShape.highlighted.bind(highlightedProperty);
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
        
        numSidesProperty.set(numSides);
        this.sideLength = sideLength;
        getChildren().clear();
        internalShape.getPoints().clear();
        controlPoints.clear();
        
        controlPoints.add(new ControlPoint(this, startPoint));
        if(endPoint != null){
            controlPoints.add(new ControlPoint(this, endPoint));

            Point2D beforeLast = startPoint;
            Point2D last = endPoint;
            Rotate angleBetweenPoints = new Rotate(((numSides-2)*-180)/numSides);
            for(int i=2;i<numSides;i++){
                Point2D newVector = angleBetweenPoints.deltaTransform(beforeLast.getX() - last.getX(),
                        beforeLast.getY() - last.getY());
                beforeLast = last;
                last = new Point2D(newVector.getX() + last.getX(), newVector.getY() + last.getY());
                controlPoints.add(new ControlPoint(this, last.getX(), last.getY()));
            }
        }
        fixToPoints();
        getChildren().add(internalShape);
        getChildren().addAll(controlPoints);
    }
    
    public List<ControlPoint> getControlPoints(){
        return controlPoints;
    }
    
    public int getNumSides(){
        return numSidesProperty.get();
    }
    
    public double getSideLength(){
        return sideLength;
    }
    
    public boolean isHighlighted(){
            return this.highlightedProperty.get();
        }

    public void setHighlighted(boolean highlighted){
        this.highlightedProperty.set(highlighted);
        controlPoints.stream().forEach((cp) -> {
            cp.setSelected(highlighted);
        });
    }
    
    public BooleanProperty highlightedProperty(){
        return highlightedProperty;
    }
    
    public boolean isRegular(){
        return regularProperty.get();
    }
    
    public void setRegular(boolean regular){
        this.regularProperty.set(regular);
    }
    
    public BooleanProperty regularProperty(){
        return regularProperty;
    }
    
    public void setNumSides(int numSides){
        numSidesProperty.set(numSides);
    }
    
    public IntegerProperty numSidesProperty(){
        return numSidesProperty;
    }
    
    public ReadOnlyStringProperty polyNameProperty(){
        return this.polygonNameProperty;
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
            internalShape.getPoints().addAll(cp.getCenterX(), cp.getCenterY());
            this.getChildren().remove(internalShape);
            this.getChildren().add(0,internalShape);
            numSidesProperty.set(controlPoints.size()-1);
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
    
    private void addListenerForCPList(){
        controlPoints.addListener((ListChangeListener.Change<? extends ControlPoint> c) -> {
            String polyName;
            switch(controlPoints.size()){
                case 2:
                    polyName = "Line";
                    break;
                case 3: 
                    polyName = "Triangle";
                    break;
                case 4:
                    polyName = "Rectangle";
                    break;
                case 5:
                    polyName = "Pentagon";
                    break;
                case 6:
                    polyName = "Hexagon";
                    break;
                default:
                    polyName = "Polygon";
                    break;
            }
            polygonNameProperty.set(polyName);
        });
    }
}
