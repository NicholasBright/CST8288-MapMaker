package mapmaker.mapelement;

import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import mapmaker.MapArea;

public final class Room
        extends Parent
        implements ModifiableProperties, TranslatableElement {
    private boolean triggerListenerFlag = true;
    
    private static PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
    
    private ObservableList<ControlPoint> controlPoints = FXCollections.observableArrayList();
    private RoomShape internalShape = new RoomShape();
    
    private final DoubleProperty sideLengthProperty = new SimpleDoubleProperty(-1){
        @Override
        public String getName(){
            return "Side length";
        }
    };
    private final StringProperty nameProperty = new SimpleStringProperty(null){
        @Override
        public String getName(){
            return "Polygon Name";
        }
        
        boolean customNameFlag = false;
        
        @Override
        public void set(String string){
            if(string != null){
                super.set(string);
                customNameFlag = true;
            }
            else if(!customNameFlag) {
                String newVal;
                switch(controlPoints.size()){
                    case 1:
                        newVal = "Point";
                        break;
                    case 2:
                        newVal = "Line";
                        break;
                    case 3: 
                        newVal = "Triangle";
                        break;
                    case 4:
                        newVal = "Rectangle";
                        break;
                    case 5:
                        newVal = "Pentagon";
                        break;
                    case 6:
                        newVal = "Hexagon";
                        break;
                    default:
                        newVal = "Polygon";
                }
                super.set(newVal);
            }
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
        propertyList.addAll(nameProperty,
                regularProperty,
                numSidesProperty,
                sideLengthProperty
        );
        return propertyList;
    }
    
    private class RoomShape extends Polygon implements RemovableElement, SelectableElement{
        
        private final BooleanProperty selectedProperty = new SimpleBooleanProperty(false) {
            @Override
            public void invalidated() {
                pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, get());
            }

            @Override
            public String getName() {
                return "Selected";
            }
            
            @Override
            public void set(boolean b){
                super.set(b);
                controlPoints.stream().forEach((cp) -> {
                    cp.setSelected(b);
                });
            }
        };
        
        public RoomShape(){
            super();
            getStyleClass().add("room");
        }
    
        @Override
        public void remove(){
            ((MapArea)(Room.this.getParent())).getRooms().remove(Room.this);
        }
    
        @Override
        public boolean isSelected(){
                return this.selectedProperty.get();
            }

        @Override
        public void setSelected(boolean selected){
            this.selectedProperty.set(selected);
        }
        
        @Override
        public void toFront(){
            Room.this.toFront();
        }
    }
    
    public Room(){
        numSidesProperty().set(0);
        numSidesProperty().addListener((o, oV, nV) -> {
            if(triggerListenerFlag){
                if((Integer)nV>1 && isRegular())
                    if((Integer)oV == 1)
                        setShape((Integer)nV, getSideLength(), controlPoints.get(0).getCenter());
                    else
                        setShape((Integer)nV, controlPoints.get(0).getCenter(), controlPoints.get(1).getCenter());
                else
                    fixToPoints();
            }
        });
        regularProperty().addListener((o, oV, nV)->{
            if(triggerListenerFlag){
                if(controlPoints.size()>1)
                    setShape(numSidesProperty().get(), controlPoints.get(0).getCenter(), controlPoints.get(1).getCenter());
            }
        });
        sideLengthProperty().addListener((o, oV, nV)->{
            if(triggerListenerFlag){
                if(controlPoints.size()>1){
                    Point2D vectorToEnd = new Point2D(
                            controlPoints.get(1).getCenterX() - controlPoints.get(0).getCenterX(),
                            controlPoints.get(1).getCenterY() - controlPoints.get(0).getCenterY());
                    vectorToEnd = vectorToEnd.normalize().multiply((Double)nV);
                    Point2D newEnd = new Point2D(controlPoints.get(0).getCenterX()+vectorToEnd.getX(),controlPoints.get(0).getCenterY()+vectorToEnd.getY());
                    setShape(numSidesProperty().get(), controlPoints.get(0).getCenter(), newEnd);
                }
            }
        });
    }
    
    public Room(Point2D startPoint){
        this(startPoint.getX(), startPoint.getY());
    }
    
    public Room(double x, double y){
        this(0, 0, x, y);
    }
    
    public Room(int numSides, double sideLength, double startX, double startY){
        this(numSides, new Point2D(startX, startY), null);
    }
    
    public Room(int numSides, double sideLength, Point2D startPoint){
        this(numSides, startPoint, new Point2D(startPoint.getX()+sideLength,startPoint.getY()));
    }
    
    public Room(int numSides, Point2D startPoint, Point2D endPoint){
        this();
        setShape(numSides, startPoint, endPoint);
    }
    
    public final void setShape(Point2D startPoint, Point2D endPoint) throws IllegalArgumentException{
        setShape(getNumSides(), startPoint, endPoint);
    }
    
    public final void setShape(int numSides, double sideLength, Point2D startPoint) throws IllegalArgumentException{
        setShape(numSides, startPoint, new Point2D(startPoint.getX()+sideLength, startPoint.getY()));
    }
    
    public final void setShape(int numSides, Point2D startPoint, Point2D endPoint) throws IllegalArgumentException{
        if(numSides < 0)
            throw new IllegalArgumentException("numSides cannot be less than 0");
        if(startPoint == null)
            throw new IllegalArgumentException("startPoint cannot be null");
        
        getChildren().clear();
        controlPoints.clear();
        
        controlPoints.add(new ControlPoint(this, startPoint));
        if(endPoint != null){
            triggerListenerFlag = false;
            numSidesProperty().set(numSides);
            sideLengthProperty.set(startPoint.distance(endPoint));
            triggerListenerFlag = true;
            
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
        nameProperty().set(null);
    }
    
    public void normalizeShape(){
        setShape(getNumSides(),
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
        return sideLengthProperty.get();
    }
    
    public void setSideLength(double sideLength){
        sideLengthProperty().set(sideLength);
    }
    
    public DoubleProperty sideLengthProperty(){
        return sideLengthProperty;
    }
    
    public boolean isSelected(){
        return selectedProperty().get();
    }
    
    public void setSelected(boolean selected){
        selectedProperty().set(selected);
    }
    
    public BooleanProperty selectedProperty(){
        return internalShape.selectedProperty;
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
    
    public String getName(){
        return nameProperty().get();
    }
    
    public void setName(String name){
        nameProperty().set(name);
    }
    
    public StringProperty nameProperty(){
        return this.nameProperty;
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
            numSidesProperty().set(controlPoints.size()-1);
        }
        nameProperty().set(null);
    }
    
    public void removeControlPoint(ControlPoint cp){
        if(getChildren().contains(cp)){
            getChildren().remove(cp);
        }
        if(controlPoints.contains(cp)){
            if(controlPoints.size() == 1)
                ((MapArea)getParent()).getRooms().remove(this);
            else {
                controlPoints.remove(cp);
                numSidesProperty().set(controlPoints.size());
            }
        }
        nameProperty().set(null);
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
}
