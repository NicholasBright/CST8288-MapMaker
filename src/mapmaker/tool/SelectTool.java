/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.tool;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import mapmaker.MapArea;
import mapmaker.mapelement.SelectableElement;

/**
 *
 * @author owner
 */
public class SelectTool extends Tool {
    Point2D startPoint;
    Rectangle selectedArea;
    boolean draggedFlag;
    
    public SelectTool(MapArea target){
        super("Select Tool", "Click and drag to select Control Points, or click on a Room to select all of it's Control Points", target);
        selectedArea = new Rectangle(0,0, 0, 10.0);
        //This style is programatically set to stop the select area from breaking out of the pane
        selectedArea.setStyle("-fx-stroke-type: inside;");
        selectedArea.setId("SelectionArea");
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        reset();
        selectedArea.setWidth(0);
        startPoint = new Point2D(e.getX(), e.getY());
        target.getChildren().add(selectedArea);
        draggedFlag = false;
        if(!e.isShiftDown())
            unselectAll(target);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!draggedFlag){
            /*target.getChildren()
                .stream()
                .forEach((n) -> {
                    if(n instanceof ){
                        Room r = (Room)n;
                        boolean clicked = (r.contains(startPoint) && r.contains(new Point2D(e.getX(), e.getY())));
                        if(!(e.isShiftDown() && !clicked))
                            r.setSelected(clicked);
                    }
                });
            target.getChildren().remove(selectedArea);*/
            if(e.getTarget() instanceof SelectableElement)
                ((SelectableElement)e.getTarget()).setSelected(true);
            if(e.getClickCount() > 1)
                if(e.getTarget() instanceof Node)
                    ((Node)e.getTarget()).toFront();
            target.getChildren().remove(selectedArea);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(draggedFlag){
            selectChildren(target);
            /*
            target.getChildren().stream().forEach((n) -> {
                if(n instanceof SelectableElement && selectedArea.getBoundsInParent().contains(n.getBoundsInParent()))
                    ((SelectableElement)n).setSelected(true);
                /*if(n instanceof Room){
                    Room r = (Room)n;
                    r.getControlPoints()
                        .stream()
                        .forEach((cp) -> {
                            boolean clicked = selectedArea.getBoundsInParent().intersects(cp.getBoundsInParent());
                            if(!(e.isShiftDown() && !clicked))
                                cp.setSelected(clicked);
                    });
                }
            });*/
            target.getChildren().remove(selectedArea);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        double eX = e.getX();
        double eY = e.getY();
        
        if(eX < target.getBoundsInLocal().getMinX())
            eX = 0;
        else if (eX > target.getBoundsInLocal().getMaxX())
            eX = target.getBoundsInLocal().getWidth();
        
        if(eX >= startPoint.getX()){
            selectedArea.setX(startPoint.getX());
            selectedArea.setWidth(eX-startPoint.getX());
        }
        else {
            selectedArea.setX(eX);
            selectedArea.setWidth(startPoint.getX()-eX);
        }
        
        if(eY < target.getBoundsInLocal().getMinY())
            eY = 0;
        else if (eY > target.getBoundsInLocal().getMaxY())
            eY = target.getBoundsInLocal().getHeight();
        
        if(eY >= startPoint.getY()){
            selectedArea.setY(startPoint.getY());
            selectedArea.setHeight(eY-startPoint.getY());
        }
        else {
            selectedArea.setY(eY);
            selectedArea.setHeight(startPoint.getY()-eY);
        }
        draggedFlag = (selectedArea.getWidth() > 5 || selectedArea.getHeight() > 5);
    }
    
    @Override
    public void mouseMoved(MouseEvent e){
        if(e.getTarget() instanceof SelectableElement)
            target.getScene().setCursor(Cursor.HAND);
        else
            target.getScene().setCursor(Cursor.DEFAULT);
    }
    
    @Override
    public void reset(){
        if(target != null){
            if(target.getChildren().contains(selectedArea))
                target.getChildren().remove(selectedArea);
        }
    }
    
    private void unselectAll(Parent p){
        p.getChildrenUnmodifiable()
            .stream()
            .forEach( (n) -> {
                if(n instanceof SelectableElement)
                    ((SelectableElement)n).setSelected(false);
                if(n instanceof Parent)
                    unselectAll((Parent)n);
            });
    }
    
    private void selectChildren(Parent p){
        if(p instanceof SelectableElement && selectedArea.getBoundsInParent().contains(p.getBoundsInParent()))
            ((SelectableElement)p).setSelected(true);
        p.getChildrenUnmodifiable()
            .stream()
            .forEach((c) -> {
                if(c instanceof SelectableElement && 
                        selectedArea.getBoundsInParent().contains(c.getBoundsInParent()))
                    ((SelectableElement)c).setSelected(true);
                if(c instanceof Parent &&
                        selectedArea.getBoundsInParent().intersects(c.getBoundsInParent()))
                    selectChildren((Parent)c);
            });
    }
}
