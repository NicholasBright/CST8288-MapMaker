/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.tool;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mapmaker.MapArea;
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
        List<Room> toRemove = new ArrayList<>();
        target.getChildren()
            .stream()
            .limit(1)
            .forEach( (n) -> {
                if(n instanceof Room){
                    Room r = (Room)n;
                    if(r.contains(e.getX(), e.getY()))
                        toRemove.add(r);
                }
            });
        target.getChildren().removeAll(toRemove);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
}
