/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.tool;

import java.util.Iterator;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mapmaker.mapelement.Room;

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
        Room toRemove = null;
        Iterator<Node> i = target.getChildren().iterator();
        while(i.hasNext()){
            Node n = i.next();
            if(n instanceof Room){
                Room r = (Room) n;
                if(r.contains(e.getX(), e.getY())){
                    toRemove = r;
                }
            }
        }
        if(toRemove != null)
            target.getChildren().remove(toRemove);
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
