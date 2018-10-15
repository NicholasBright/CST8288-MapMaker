package mapmaker.tool;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.scene.input.MouseEvent;
import mapmaker.MapArea;

/**
 *
 * @author Nicholas Bright
 */
public abstract class Tool{
    protected String name;
    protected SimpleStringProperty nameProperty;
    protected String description;
    protected SimpleStringProperty descProperty;
    protected MapArea target;
    
    
    protected Tool(String toolName, String toolDescription, MapArea target){
        this.target = target;
        name = toolName;
        description = toolDescription;
        nameProperty = new SimpleStringProperty() {
            @Override
            public String get() {
                return name;
            }
        };
        descProperty = new SimpleStringProperty() {
            @Override
            public String get() {
                return description;
            }
        };
    }
    
    public final StringPropertyBase getNameProperty(){
        return nameProperty;
    }
    
    public final StringPropertyBase getDescriptionProperty(){
        return descProperty;
    }
    
    public abstract void mousePressed (MouseEvent e);
    public abstract void mouseClicked (MouseEvent e);
    public abstract void mouseReleased (MouseEvent e);
    public abstract void mouseDragged (MouseEvent e);
    public abstract void mouseMoved (MouseEvent e);
    public abstract void reset();
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Tool){
            Tool t = (Tool) o;
            if(t.getNameProperty().get().equals(getNameProperty().get()));
        }
        return false;
    }
    
    @Override
    public String toString(){
        return name;
    }
    
    public void handleMouseEvent(MouseEvent e){
        if(MouseEvent.MOUSE_PRESSED.equals(e.getEventType()))
            mousePressed(e);
        else if(MouseEvent.MOUSE_CLICKED.equals(e.getEventType()))
            mouseClicked(e);
        else if(MouseEvent.MOUSE_RELEASED.equals(e.getEventType()))
            mouseReleased(e);
        else if(MouseEvent.MOUSE_DRAGGED.equals(e.getEventType()))
            mouseDragged(e);
        else if(MouseEvent.MOUSE_MOVED.equals(e.getEventType()))
            mouseMoved(e);
    }
}
