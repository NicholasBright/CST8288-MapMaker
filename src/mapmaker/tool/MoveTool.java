package mapmaker.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mapmaker.mapelement.ControlPoint;
import mapmaker.mapelement.TranslatableElement;
import mapmaker.mapelement.Room;
import mapmaker.mapelement.SelectableElement;

/**
 *
 * @author owner
 */
public class MoveTool extends Tool {
    Point2D lastPoint = null;
    final ArrayList<TranslatableElement> toMoveList = new ArrayList<>();
    
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
    
    private void cleanUpControlPointOfRegularRooms(){
        ArrayList<Room> toAdd = new ArrayList<>();
        ArrayList<ControlPoint> toRemove = new ArrayList<>();
        toMoveList.stream()
            .filter((te)->(te instanceof ControlPoint))
            .forEach((te)->{
                ControlPoint cp = ((ControlPoint)te);
                Room owner = cp.getOwner();
                if(owner.isRegular()){
                    if(!toAdd.contains(owner))
                        toAdd.add(owner);
                    owner.getControlPoints()
                        .stream()
                        .filter((c)->(!toRemove.contains(c)))
                        .forEach((c)->{
                            toRemove.add(c);
                        });
                }
            });
        toMoveList.addAll(toAdd);
        toMoveList.removeAll(toRemove);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        lastPoint = new Point2D(e.getX(), e.getY());
        
        getSelectedChildren(target).stream()
            .filter((child)->(child instanceof TranslatableElement))
            .forEach((child)->{
                toMoveList.add((TranslatableElement)child);
            });
        
        if(!(toMoveList.size() > 0)){
            target.getChildren().stream()
                    .filter((child)->(child instanceof TranslatableElement))
                    .filter((child)->(child.contains(e.getX(), e.getY())))
                    .findFirst().ifPresent((c)->{
                        toMoveList.add((TranslatableElement)c);
                    });
        }
        
        cleanUpControlPointOfRegularRooms();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        toMoveList.clear();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        toMoveList.stream().forEach((te)->{
            if(te instanceof ControlPoint)
                if(((ControlPoint) te).getOwner().isRegular())
            te.translate(e.getX()-lastPoint.getX(),e.getY()-lastPoint.getY());
        });
        lastPoint = new Point2D(e.getX(), e.getY());
    }
    
    @Override
    public void reset() {
    }
}
