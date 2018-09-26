package mapmaker.tool;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Nicholas Bright
 */
public abstract class Tool{
    protected static Tool t;
    protected String name;
    protected SimpleStringProperty nameProperty;
    protected String description;
    protected SimpleStringProperty descProperty;
    
    
    protected Tool(String toolName, String toolDescription){
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
    
    public abstract void mousePressed (MouseEvent e);
    public abstract void mouseClicked (MouseEvent e);
    public abstract void mouseReleased (MouseEvent e);
    public abstract void mouseDragged (MouseEvent e);
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Tool){
            Tool t = (Tool) o;
            if(t.getNameProperty().get().equals(getNameProperty().get()));
        }
        return false;
    }
}
