package mapmaker.tool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mapmaker.mapelement.ControlPoint;
import mapmaker.mapelement.Room;
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
            return cp.getOwner();
        return cp;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        target.getScene().setCursor(Cursor.MOVE);
        lastPoint = new Point2D(e.getX(), e.getY());
                
        getSelectedChildren(target).stream()
            .filter((child)->(child instanceof TranslatableElement))
            .forEach((child)->{
                if(child instanceof ControlPoint)
                    toMoveSet.add((TranslatableElement) addPointOrRoom((ControlPoint)child));
                else if(!toMoveSet.contains((TranslatableElement)child))
                    toMoveSet.add((TranslatableElement)child);
            });
        
        Iterator<Node> iterN = target.getChildren().iterator();
        while(iterN.hasNext() && toMoveSet.size() < 1){
            Node n = iterN.next();
            if(n instanceof TranslatableElement){
                if(n instanceof Room){
                    Iterator<ControlPoint> iterCP = ((Room)n).getControlPoints().iterator();
                    while(iterCP.hasNext() && toMoveSet.size() < 1){
                        ControlPoint cp = iterCP.next();
                        if(cp.contains(lastPoint)){
                            toMoveSet.add((TranslatableElement)addPointOrRoom(cp));
                        }
                    }
                }
                if(toMoveSet.size() < 1 && n.contains(lastPoint))
                    toMoveSet.add((TranslatableElement)n);
            }
        }
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
    public void reset() {
    }
}
