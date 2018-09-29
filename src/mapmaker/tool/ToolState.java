/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author owner
 */
public final class ToolState {
    private static final ToolState TS = new ToolState();
    
    private final List<Object> options;
    
    private Tool activeTool;
    
    private final ObjectProperty<Tool> activeToolProperty;
    
    private ToolState(){
        this.options = new ArrayList<>();
        activeToolProperty = new SimpleObjectProperty<Tool>(){
            @Override
            public Tool get(){
                return activeTool;
            }
            
            @Override
            public void set(Tool t){
                if(activeTool != null)
                    activeTool.reset();
                activeTool = t;
                fireValueChangedEvent();
            }
        };
    }
    
    public static ToolState getToolState(){return TS;}
    
    public void setActiveTool(Tool tool, Object ... optionList){
        activeToolProperty.set(tool);
        options.clear();
        options.addAll(Arrays.asList(optionList));
    }
    
    public Tool getActiveTool(){
        return activeToolProperty.get();
    }
    
    public ObjectProperty<Tool> getActiveToolProperty(){
        return activeToolProperty;
    }
    
    public Object getOption(int index){
        return options.get(index);
    }
    
    public List<Object> getOptions(){
        return options;
    }
}
