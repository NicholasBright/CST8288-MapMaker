/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.tool;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mapmaker.mapelement.RemovableElement;

/**
 *
 * @author owner
 */
public class EraseTool extends Tool {
    
    public EraseTool(Pane target){
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
