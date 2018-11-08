/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.mapelement;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

/**
 *
 * @author owner
 */
public class EndPoint extends Circle implements RemovableElement, TranslatableElement {
    Path path;
    
    public EndPoint(Path path) {
        super.setRadius(3.5);
        super.getStyleClass().addAll("end-point");
        this.path = path;
    }

    @Override
    public List<Node> remove() {
        return path.remove();
    }
    
    @Override
    public void translate(double x, double y){
        path.translate(x, y);
    }
}
