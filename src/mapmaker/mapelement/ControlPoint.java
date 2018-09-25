/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.mapelement;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

/**
 *
 * @author owner
 */
public class ControlPoint extends Circle {
    Room owner;
    
    private static PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
    
    public ControlPoint (Room owner){
        this(owner, 100.0,100.0);
    }
    
    public ControlPoint(Room owner, Point2D pos){
        this(owner, pos.getX(), pos.getY());
    }
    
    public ControlPoint(Room owner, Double x, Double y){
        super(x, y, 3.5);
        this.getStyleClass().add("control-point");
        this.owner = owner;
    }
    
    private final BooleanProperty selected = new BooleanPropertyBase(false) {
        @Override
        public void invalidated() {
            pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, get());
        }
        
        @Override
        public Object getBean() {
            return ControlPoint.this;
        }

        @Override
        public String getName() {
            return "selected";
        }
    };
    
    public boolean isSelected() {return this.selected.get();}
    
    public void setSelected(boolean selected){
        this.selected.set(selected);
    }
    
    public void setPosition(Point2D point){
        setPosition(point.getX(), point.getY());
    }
    
    public void setPosition(Double x, Double y){
        setCenterX(x);
        setCenterY(y);
        owner.updateShape();
    }
    
    public void setX(double x){
        super.setCenterX(x);
        owner.updateShape();
    }
    
    public void setY(double y){
        super.setCenterY(y);
        owner.updateShape();
    }
}
