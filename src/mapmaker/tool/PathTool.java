/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.tool;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import mapmaker.MapArea;
import mapmaker.mapelement.Path;
import mapmaker.mapelement.Room;

/**
 *
 * @author nick
 */
public class PathTool extends Tool {
    Room start, end;
    Line visual;
    
    public PathTool(MapArea target){
        super("Path Tool","Click from one Door to another to create a path", target);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getTarget() instanceof Room){
            start = Room.class.cast(e.getTarget());
            visual = new Line(e.getX(),e.getY(),e.getX(),e.getY());
            target.getChildren().add(visual);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        target.getChildren().remove(visual);
        for(Room r : target.getRooms()){
            if(r.contains(e.getX(), e.getY())){
                end = r;
                break;
            }
        }
        if(start == null || end == null)
            return;
        Path p = new Path(start, end);
        target.getChildren().add(p);
        target.getChildren().add(p.getStartPoint());
        target.getChildren().add(p.getEndPoint());
        start.addPath(p);
        end.addPath(p);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        visual.setEndX(e.getX());
        visual.setEndY(e.getY());
    }
    
    @Override
    public void mouseMoved(MouseEvent e){
    }
    
    @Override
    public void reset() {
    }
}
