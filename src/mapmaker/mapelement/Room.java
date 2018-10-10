/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.mapelement;

import java.util.HashMap;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.css.PseudoClass;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

/**
 *
 * @author nick
 */
public final class Room extends Parent implements ModifiableProperties, TranslatableElement, SelectableElement {
    private static PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
    
    private double sideLength;
    private ObservableList<ControlPoint> controlPoints = FXCollections.observableArrayList();
    private RoomShape internalShape = new RoomShape();
    
    private final SimpleStringProperty polygonNameProperty = new SimpleStringProperty(){
        @Override
        public String getName(){
            return "Polygon Name";
        }
    };
    
    private final BooleanProperty selectedProperty = new SimpleBooleanProperty(false){
        @Override
        public String getName(){
            return "Selected";
        }
    };
    private final BooleanProperty regularProperty = new SimpleBooleanProperty(true){
        @Override
        public String getName(){
            return "Regular";
        }
    };
    private final IntegerProperty numSidesProperty = new SimpleIntegerProperty(){
        @Override
        public String getName(){
            return "Number of Sides";
        }
    };

    @Override
    public ObservableList<Property> getModifiablePropertiesList() {
        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.addAll(
                polygonNameProperty,
                regularProperty,
                numSidesProperty
        );
        return propertyList;
    }
    
    private class RoomShape extends Polygon{
        
        private final BooleanProperty selected = new SimpleBooleanProperty(false) {
            @Override
            public void invalidated() {
                pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, get());
            }

            @Override
            public Object getBean() {
                return this;
            }

            @Override
            public String getName() {
                return "Selected";
            }
        };
        
        private final ObservableMap<String,String> styleStringsProperty = FXCollections.observableMap((new HashMap<>()));
        
        public RoomShape(){
            super();
            getStyleClass().add("room");
            styleStringsProperty.addListener((MapChangeListener.Change<? extends String,? extends String> c)->{
                StringBuilder newStyleStringBuilder = new StringBuilder();
                c.getMap().forEach((k,v) -> {
                    if(!(v == null || v.equals(""))){
                        newStyleStringBuilder.append(k);
                        newStyleStringBuilder.append(": ");
                        newStyleStringBuilder.append(v);
                        newStyleStringBuilder.append(";");
                    }
                });
                styleProperty().set(newStyleStringBuilder.toString());
            });
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
        numSidesProperty.set(numSides);
        this.sideLength = sideLength;
        setShape(numSides, sideLength, startPoint, firstSideEndPoint);
        addListenerForCPList();
        numSidesProperty().addListener((o, oV, nV) -> {
            setShape();
        });
        internalShape.selected.bind(selectedProperty);
        internalShape.styleStringsProperty.put("-fx-fill", "");
    }
    
    public final void setShape(){
        setShape(numSidesProperty().get(),sideLength, controlPoints.get(0).getCenter(), controlPoints.get(1).getCenter());
    }
    
    public final void setShape(Point2D startPoint, Point2D endPoint) throws IllegalArgumentException{
        setShape(getNumSides(), endPoint.distance(startPoint), startPoint, endPoint);
    }
    
    public final void setShape(Point2D startPoint) throws IllegalArgumentException{
        setShape(getNumSides(), getSideLength(), startPoint);
    }
    
    public final void setShape(int numSides, double sideLength, Point2D startPoint) throws IllegalArgumentException{
        setShape(numSides, sideLength, startPoint, new Point2D(startPoint.getX()+sideLength, startPoint.getY()));
    }
    
    public final void setShape(int numSides, double sideLength, Point2D startPoint, Point2D endPoint) throws IllegalArgumentException{
        if(numSides < 0)
            throw new IllegalArgumentException("numSides cannot be less than 0");
        if(sideLength < 0)
            throw new IllegalArgumentException("sideLength cannot be less than 0");
        if(startPoint == null)
            throw new IllegalArgumentException("startPoint cannot be null");
        
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
        
        controlPoints.forEach(cp -> cp.setSelected(selectedProperty().get()));
        
        fixToPoints();
        getChildren().add(internalShape);
        getChildren().addAll(controlPoints);
    }
    
    public void normalizeShape(){
        setShape(getNumSides(), getSideLength(),
                new Point2D(internalShape.getPoints().get(0),
                            internalShape.getPoints().get(1)),
                new Point2D(internalShape.getPoints().get(2),
                            internalShape.getPoints().get(3)));
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
    
    @Override
    public boolean isSelected(){
            return this.selectedProperty.get();
        }

    @Override
    public void setSelected(boolean selected){
        this.selectedProperty.set(selected);
        controlPoints.stream().forEach((cp) -> {
            cp.setSelected(selected);
        });
    }
    
    public BooleanProperty selectedProperty(){
        return selectedProperty;
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
    
    @Override
    public void translate(double xTrans, double yTrans){
        Bounds bounds = getBoundsInParent();
        
        if(bounds.getMinX()+xTrans < 0)
            xTrans = bounds.getMinX() * -1;
        
        if(bounds.getMinY()+yTrans < 0)
            yTrans = bounds.getMinY() * -1;
        
        double x = xTrans;
        double y = yTrans;
        controlPoints.stream().forEach((cp) -> {
            cp.translate(x, y);
        });
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
