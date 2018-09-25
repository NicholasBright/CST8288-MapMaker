/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.tool;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.MouseEvent;
import mapmaker.MapArea;
import mapmaker.mapelement.Room;

/**
 *
 * @author owner
 */
public class EraseTool extends Tool {
    private static final EraseTool ET = new EraseTool();
    
    private EraseTool(){
        super("Erase Tool", "Click on any room to erase it");
        t = ET;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        List<Room> toRemove = new ArrayList<>();
        MapArea.getRooms()
            .stream()
            .filter( (r) -> (r.contains(e.getX(), e.getY())))
            .limit(1)
            .forEach( (r) -> {
                toRemove.add(r);
            });
        MapArea.removeAll(toRemove);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
    
    public static Tool getTool(){
        return ET;
    }
}
