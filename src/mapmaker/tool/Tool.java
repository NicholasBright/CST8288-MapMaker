package mapmaker.tool;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author Nicholas Bright
 */
public abstract class Tool{
    protected Pane target;
    
    public Tool(){
        
    }
    
    public Tool(Pane target){
        setTarget(target);
    }
    
    public void setTarget(Pane target){
        this.target = target;
    }
    
    public abstract void mousePressed (MouseEvent e);
    public abstract void mouseClicked (MouseEvent e);
    public abstract void mouseReleased (MouseEvent e);
    public abstract void mouseDragged (MouseEvent e);
}
