package mapmaker.tool;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author Nicholas Bright
 */
public abstract class Tool{
    protected Pane target;
    protected SimpleStringProperty nameProperty;
    
    protected Tool(){
    }
    
    public Tool(Pane target, String toolName){
        setTarget(target);
        nameProperty = new SimpleStringProperty() {
            @Override
            public String getName() {
                return toolName;
            }
        };
    }
    
    public void setTarget(Pane target){
        this.target = target;
    }
    
    public final StringPropertyBase getNameProperty(){
        return nameProperty;
    }
    
    public abstract void mousePressed (MouseEvent e);
    public abstract void mouseClicked (MouseEvent e);
    public abstract void mouseReleased (MouseEvent e);
    public abstract void mouseDragged (MouseEvent e);
}
