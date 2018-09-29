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
public class PathTool extends Tool {
    
    public PathTool(Pane target){
        super("Path Tool","Click from one Door to another to create a path", target);
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
