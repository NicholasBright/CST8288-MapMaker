/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.tool;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import mapmaker.mapelement.Room;

/**
 *
 * @author owner
 */
public class SelectTool extends Tool {
    Point2D startPoint;
    Rectangle selectedArea;
    
    public SelectTool(Pane target){
        super("Select Tool", "Click and drag to select Control Points, or click on a Room to select all of it's Control Points", target);
        selectedArea = new Rectangle(0,0, 0, 10.0);
        //This style is programatically set to stop the select area from breaking out of the pane
        selectedArea.setStyle("-fx-stroke-type: inside;");
        selectedArea.setId("SelectionArea");
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        reset();
        unselectPoints();
        selectedArea.setWidth(0);
        startPoint = new Point2D(e.getX(), e.getY());
        target.getChildren().add(selectedArea);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        target.getChildren()
            .stream()
            .forEach((n) -> {
                if(n instanceof Room){
                    Room r = (Room)n;
                    if(r.contains(startPoint) && r.contains(new Point2D(e.getX(), e.getY()))){
                        r.getControlPoints()
                            .stream()
                            .forEach((cp) -> {
                                cp.setSelected(true);
                            });
                        r.setHighlighted(true);
                    }
                }
            });
        target.getChildren().remove(selectedArea);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        target.getChildren().stream().forEach((n) -> {
            if(n instanceof Room){
                Room r = (Room)n;
                r.getControlPoints()
                    .stream()
                    .filter((cp) -> (selectedArea.getBoundsInParent().intersects(cp.getBoundsInParent())))
                    .forEach((cp) -> {
                        cp.setSelected(true);
                });
            }
        });
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
    }
    
    @Override
    public void reset(){
        if(target != null){
            if(target.getChildren().contains(selectedArea))
                target.getChildren().remove(selectedArea);
        }
    }
    
    public void unselectPoints(){
        target.getChildren()
            .stream()
            .forEach( (n) -> {
                if(n instanceof Room){
                    Room r = (Room)n;
                    r.getControlPoints()
                        .stream()
                        .forEach( (cp) -> {
                            cp.setSelected(false);
                        });
                }
            });
    }
}
