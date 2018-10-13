package mapmaker.tool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mapmaker.mapelement.ControlPoint;
import mapmaker.mapelement.TranslatableElement;
import mapmaker.mapelement.SelectableElement;

/**
 *
 * @author owner
 */
public class MoveTool extends Tool {
    Point2D lastPoint = null;
    final Set<TranslatableElement> toMoveSet = new HashSet<>();
    
    public MoveTool(Pane target){
        super("Move Tool", "Click and drag to move selected control points around the pane", target);
    }
    
    private List<SelectableElement> getSelectedChildren(Parent parent){
        ArrayList<SelectableElement> finalList = new ArrayList<>();
        parent.getChildrenUnmodifiable()
            .stream()
            .forEach((child)->{
                if(child instanceof Parent)
                    finalList.addAll(getSelectedChildren((Parent)child));
                if(child instanceof SelectableElement)
                    if(((SelectableElement)child).isSelected())
                        finalList.add((SelectableElement)child);
            });
        return finalList;
    }
    
    private Node addPointOrRoom(ControlPoint cp){
        if(cp.getOwner().isRegular())
            return cp.getOwner().getShape();
        return cp;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        lastPoint = new Point2D(e.getX(), e.getY());
                
        getSelectedChildren(target).stream()
            .filter((child)->(child instanceof TranslatableElement))
            .forEach((child)->{
                if(child instanceof ControlPoint)
                    toMoveSet.add((TranslatableElement) addPointOrRoom((ControlPoint)child));
                else if(!toMoveSet.contains((TranslatableElement)child))
                    toMoveSet.add((TranslatableElement)child);
            });
        
        if(toMoveSet.isEmpty())
            if(e.getTarget() instanceof TranslatableElement){
                if(e.getTarget() instanceof ControlPoint)
                    toMoveSet.add(TranslatableElement.class.cast(addPointOrRoom(ControlPoint.class.cast(e.getTarget()))));
                else
                    toMoveSet.add(TranslatableElement.class.cast(e.getTarget()));
            }
        
        if(toMoveSet.size() > 0)
            target.getScene().setCursor(Cursor.MOVE);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        toMoveSet.clear();
        target.getScene().setCursor(Cursor.DEFAULT);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        toMoveSet.stream().forEach((te)->{
            te.translate(e.getX()-lastPoint.getX(),e.getY()-lastPoint.getY());
        });
        lastPoint = new Point2D(e.getX(), e.getY());
    }
    
    @Override
    public void mouseMoved(MouseEvent e){
        
    }
    
    @Override
    public void reset() {
    }
}
