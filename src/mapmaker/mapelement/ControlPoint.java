/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.mapelement;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

/**
 *
 * @author owner
 */
public class ControlPoint extends Circle implements TranslatableElement, SelectableElement, RemovableElement {
    Room owner;
    
    private static PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
    
    private final BooleanProperty selected = new SimpleBooleanProperty(false) {
        @Override
        public void invalidated() {
            pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, get());
        }

        @Override
        public String getName() {
            return "Selected";
        }
        
        @Override
        public void set(boolean val){
            if(owner.isRegular() && owner.isSelected() != val)
                owner.setSelected(val);
            else
                super.set(val);
        }
    };
    
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
    
    @Override
    public boolean isSelected() {return this.selected.get();}
    
    @Override
    public void setSelected(boolean selected){
        this.selected.set(selected);
    }
    
    public Point2D getCenter(){
        return new Point2D(getCenterX(), getCenterY());
    }
    
    @Override
    public void translate(double xTrans, double yTrans){
        setCenterX(getCenterX() + xTrans);
        setCenterY(getCenterY() + yTrans);
    }
    
    public Room getOwner(){
        return owner;
    }
    
    @Override
    public void remove(){
        getOwner().removeControlPoint(this);
    }
}
