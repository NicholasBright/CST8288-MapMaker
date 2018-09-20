/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.tool;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author owner
 */
public class SelectTool extends Tool {
    Point2D startPoint;
    Rectangle selectedArea;
    
    @Override
    public void mousePressed(MouseEvent e) {
        selectedArea = new Rectangle(e.getX(),e.getY(), 0, 10.0);
        //This style is programatically set to stop the select area from breaking out of the pane
        selectedArea.setStyle("-fx-stroke-type: inside;");
        selectedArea.setId("SelectionArea");
        startPoint = new Point2D(e.getX(), e.getY());
        target.getChildren().add(selectedArea);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        return;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        target.getChildren().remove(selectedArea);
        selectedArea = null;
        startPoint = null;
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
    
}
