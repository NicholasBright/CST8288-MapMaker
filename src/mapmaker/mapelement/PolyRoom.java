package mapmaker.mapelement;

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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import mapmaker.MapArea;

public final class PolyRoom
        extends Polygon
        implements ModifiableProperties, TranslatableElement, SelectableElement, RemovableElement {
    private boolean triggerListenerFlag = true;
    
    private static PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
    
    private Group controlPointGroup = new Group(){};
    
    private ObservableList<ControlPoint> controlPoints = FXCollections.observableArrayList();
    
    private final DoubleProperty radiusProperty = new SimpleDoubleProperty(0.0){
        @Override
        public String getName(){
            return "Radius";
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
                switch(getNumSides()){
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
                radiusProperty,
                xProperty,
                yProperty
        );
        return propertyList;
    }
    
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
    
    private final DoubleProperty xProperty = new SimpleDoubleProperty(){
        @Override
        public String getName(){
            return "X";
        }
    };
    
    private final DoubleProperty yProperty = new SimpleDoubleProperty(){
        @Override
        public String getName(){
            return "Y";
        }
    };
    
    public PolyRoom(){
        this(0.0, 0.0);
    }
    
    public PolyRoom(double centerX, double centerY){
        this(centerX, centerY, 0.0);
    }
    
    public PolyRoom(double centerX, double centerY, double radius){
        this(centerX, centerY, radius, 0);
    }
    
    public PolyRoom(double centerX, double centerY, double radius, int numSides){
        triggerListenerFlag = false;
        
        initializeListeners();
        
        xProperty.set(centerX);
        yProperty.set(centerY);
        radiusProperty.set(radius);
        numSidesProperty.set(numSides);
        
        for(int i=0;i<numSides;i++){
            controlPoints.add(initializeNewControlPoint());
        }
        
        getStyleClass().add("room");
        triggerListenerFlag = true;
    }
    
    /*public void redraw(){
        redraw(getCenterX(), getCenterY(), getCenterX(), getCenterY() + getRadius());
    }
    
    public void redraw(double firstX, double firstY){
        redraw(getCenterX(), getCenterY(), firstX, firstY);
    }
    
    public void redraw(double centerX, double centerY, double firstX, double firstY){
        if(getNumSides() < 1)
            return;
        
        setRadius((new Point2D(firstX, firstY)).distance(getCenterX(), getCenterY()));
        xProperty.set(centerX);
        yProperty.set(centerY);
        
        controlPoints.clear();
        
        ControlPoint firstPoint = new ControlPoint(this, firstX, firstY);
        firstPoint.centerXProperty().addListener((o, oV, nV)->{
            if(triggerListenerFlag){
                if(isRegular())
                    redraw(Double.class.cast(nV), firstPoint.getCenterY());
                else
                    getPoints().set(controlPoints.indexOf(firstPoint), Double.class.cast(nV));
            }
        });
        firstPoint.centerYProperty().addListener((o, oV, nV)->{
            if(triggerListenerFlag){
                if(isRegular())
                    redraw(firstPoint.getCenterX(), Double.class.cast(nV));
                else
                    getPoints().set(controlPoints.indexOf(firstPoint)+1, Double.class.cast(nV));
            }
        });
        controlPoints.add(firstPoint);
        
        Rotate angleBetweenPoints = new Rotate(180 - (getNumSides()-2)*-180/getNumSides());
        Point2D lastVector = angleBetweenPoints.deltaTransform(firstX - centerX, firstY - centerY);
        for(int i=1;i<getNumSides();i++){
            double x = lastVector.getX() + centerX;
            double y = lastVector.getY() + centerY;
            
            ControlPoint newPoint = new ControlPoint(this, x, y);
            newPoint.centerXProperty().addListener((o, oV, nV)->{
                if(triggerListenerFlag){
                    if(isRegular())
                        redraw(Double.class.cast(nV), newPoint.getCenterY());
                    else
                        getPoints().set(controlPoints.indexOf(newPoint), Double.class.cast(nV));
                }
            });
            newPoint.centerYProperty().addListener((o, oV, nV)->{
                if(triggerListenerFlag){
                    if(isRegular())
                        redraw(newPoint.getCenterX(), Double.class.cast(nV));
                    else
                        getPoints().set(controlPoints.indexOf(newPoint)+1, Double.class.cast(nV));
                }
            });
            
            controlPoints.add(newPoint);
            lastVector = angleBetweenPoints.deltaTransform(lastVector.getX(), lastVector.getY());
        }
        
        controlPoints.forEach(cp -> cp.setSelected(selectedProperty().get()));
        
        fixToPoints();
        nameProperty().set(null);
    }*/
    
    public void normalizeShape(){
        triggerListenerFlag = false;
        normalizeShape(controlPoints.get(0).getCenterX(), controlPoints.get(0).getCenterY());
        triggerListenerFlag = true;
    }
    
    public void normalizeShape(double firstX, double firstY){
        triggerListenerFlag = false;
        normalizeShape(getCenterX(), getCenterY(), firstX, firstY);
        triggerListenerFlag = true;
    }
    
    public void normalizeShape(double centerX, double centerY, double firstX, double firstY){
        if(getNumSides() < 1)
            return;
        
        setCenterX(centerX);
        setCenterY(centerY);
        
        triggerListenerFlag = false;
        setRadius((new Point2D(firstX, firstY)).distance(getCenterX(), getCenterY()));
        
        ControlPoint firstPoint = controlPoints.get(0);
        firstPoint.setCenterX(firstX);
        firstPoint.setCenterY(firstY);
        
        Rotate angleBetweenPoints = new Rotate(180 - (getNumSides()-2)*-180/getNumSides());
        Point2D lastVector = angleBetweenPoints.deltaTransform(firstX - getCenterX(), firstY - getCenterY());
        for(int i=1;i<getNumSides();i++){
            double x = lastVector.getX() + getCenterX();
            double y = lastVector.getY() + getCenterY();
            
            ControlPoint currPoint = controlPoints.get(i);
            currPoint.setCenterX(x);
            currPoint.setCenterY(y);
            
            lastVector = angleBetweenPoints.deltaTransform(lastVector.getX(), lastVector.getY());
        }
        
        fixToPoints();
        triggerListenerFlag = true;
    }
    
    public ObservableList<ControlPoint> getControlPointList(){
        return controlPoints;
    }
    
    public Parent getControlPoints(){
        return controlPointGroup;
    }
    
    public int getNumSides(){
        return numSidesProperty.get();
    }
    
    @Override
    public boolean isSelected(){
        return selectedProperty().get();
    }
    
    @Override
    public void setSelected(boolean selected){
        selectedProperty().set(selected);
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
        getPoints().clear();
        controlPoints
            .stream()
            .forEach( (cp) -> {
                getPoints().addAll(cp.getCenterX(), cp.getCenterY());
            });
    }
    
    public void addControlPoint(double x, double y) {
        triggerListenerFlag = false;
        ControlPoint cp = initializeNewControlPoint();
        cp.setCenterX(x);
        cp.setCenterY(y);
        controlPoints.add(cp);
        if(isRegular()){
            normalizeShape();
        } else {
            getPoints().addAll(cp.getCenterX(), cp.getCenterY()); 
        }
        numSidesProperty().set(controlPoints.size()-1);
        nameProperty().set(null);
        triggerListenerFlag = true;
    }
    
    public void removeControlPoint(ControlPoint cp){
        if(controlPoints.contains(cp)){
            if(controlPoints.size() < 3)
                ((MapArea)getParent()).remove(this);
            else {
                controlPoints.remove(cp);
                numSidesProperty().set(controlPoints.size());
                normalizeShape();
            }
        }
        nameProperty().set(null);
    }
    
    @Override
    public void translate(double xTrans, double yTrans){
        triggerListenerFlag = false;
        double x = xTrans;
        double y = yTrans;
        setCenterX(getCenterX() + xTrans);
        setCenterY(getCenterY() + yTrans);
        controlPoints.stream().forEach((cp)->{
            cp.translate(xTrans, yTrans);
        });
        fixToPoints();
        triggerListenerFlag = true;
    }

    @Override
    public void toFront(){
        super.toFront();
        controlPoints.stream().forEach((cp)->{cp.toFront();});
    }
    
    public void setRadius(Double radius){
        radiusProperty.set(radius);
    }
    
    public double getRadius(){
        return radiusProperty.get();
    }
    
    public DoubleProperty radiusProperty(){
        return radiusProperty;
    }
    
    public void setCenterX(double x){
        xProperty.set(x);
    }
    
    public double getCenterX(){
        return xProperty.get();
    }
    
    public DoubleProperty centerXProperty(){
        return xProperty;
    }
    
    public double getCenterY(){
        return yProperty.get();
    }
    
    public void setCenterY(double y){
        yProperty.set(y);
    }
    
    public DoubleProperty centerYProperty(){
        return yProperty;
    }
    
    @Override
    public String toString(){
        return "Room[centerX:" + xProperty.get() +
                ", centerY: " + yProperty.get() +
                ", radius: " + radiusProperty.get()
                + ", numSides: " + numSidesProperty.get() + "]";
    }
    
    private ControlPoint initializeNewControlPoint(){
        ControlPoint newCP = new ControlPoint(this);
        newCP.setCenterX(getCenterX());
        newCP.setCenterY(getCenterY());
        newCP.centerXProperty().addListener((o, oV, nV)->{
            if(triggerListenerFlag){
                if(isRegular()){
                    while(!controlPoints.get(0).equals(newCP))
                        controlPoints.add(controlPoints.remove(0));
                    normalizeShape(Double.class.cast(nV), newCP.getCenterY());
                } else
                    getPoints().set(controlPoints.indexOf(newCP)*2, Double.class.cast(nV));
            }
        });
        newCP.centerYProperty().addListener((o, oV, nV)->{
            if(triggerListenerFlag){
                if(isRegular()){
                    while(!controlPoints.get(0).equals(newCP))
                        controlPoints.add(controlPoints.remove(0));
                    normalizeShape(newCP.getCenterX(), Double.class.cast(nV));
                } else
                    getPoints().set(controlPoints.indexOf(newCP)*2+1, Double.class.cast(nV));
            }
        });
        return newCP;
    }
    
    private void initializeListeners(){
        numSidesProperty.addListener((o, oV, nV)->{
            nameProperty.set(null);
            if(triggerListenerFlag){
                if(nV.intValue() > controlPoints.size()){
                    int i = nV.intValue() - controlPoints.size();
                    while(i-- > 0){
                        controlPoints.add(initializeNewControlPoint());
                    }
                    normalizeShape();
                }
                else if(nV.intValue() < controlPoints.size()){
                    int i = controlPoints.size() - nV.intValue();
                    while(i-- > 0){
                        controlPoints.remove(controlPoints.size()-1);
                    }
                    normalizeShape();
                }
            }
        });
        controlPoints.addListener((ListChangeListener.Change<? extends ControlPoint> c)->{
            while(c.next()){
                c.getAddedSubList().stream().forEach((cp)->{
                    controlPointGroup.getChildren().add(cp);
                });
                c.getRemoved().stream().forEach((cp)->{
                    controlPointGroup.getChildren().remove(cp);
                });
            }
        });
        radiusProperty.addListener((o, oV, nV) -> {
            if(triggerListenerFlag){
                Point2D vectorToFirstCP = 
                    new Point2D(controlPoints.get(0).getCenterX() - getCenterX(), 
                                controlPoints.get(0).getCenterY() - getCenterY())
                        .normalize().multiply(nV.doubleValue());
                triggerListenerFlag = false;
                normalizeShape(vectorToFirstCP.getX() + getCenterX(), vectorToFirstCP.getY() + getCenterY());
                triggerListenerFlag = true;
            }
        });
        xProperty.addListener((o, oV, nV)->{
            if(triggerListenerFlag){
                triggerListenerFlag = false;
                translate(nV.doubleValue()-oV.doubleValue(),0);
                triggerListenerFlag = true;
            }
        });
    }
    
    @Override
    public void remove(){
        if(getParent() instanceof MapArea){
            MapArea.class.cast(getParent()).remove(this);
        }
    }
}
