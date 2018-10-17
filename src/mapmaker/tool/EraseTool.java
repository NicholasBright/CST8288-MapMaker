/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.tool;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import mapmaker.MapArea;
import mapmaker.mapelement.RemovableElement;

/**
 *
 * @author owner
 */
public class EraseTool extends Tool {
    
    public EraseTool(MapArea target){
        super("Erase Tool", "Click on any room to erase it", target);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getTarget() instanceof RemovableElement){
            ((RemovableElement)e.getTarget()).remove();
        }
        else {
            target.getChildren().remove(Node.class.cast(e.getTarget()));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
    @Override
    public void reset() {
    }
}
