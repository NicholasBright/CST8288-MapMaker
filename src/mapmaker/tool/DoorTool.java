/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.tool;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author nick
 */
public class DoorTool extends Tool {
    
    public DoorTool(Pane target){
        super("Door Tool", "Click along the edge of a room to create a door there", target);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
    
    @Override
    public void reset() {
    }
    
}
