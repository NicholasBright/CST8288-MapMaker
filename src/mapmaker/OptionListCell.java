/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

/**
 *
 * @author owner
 */
public class OptionListCell extends ListCell<Pair<String,Node>> {
    
    private final HBox cellBox = new HBox();
    
    @Override
    protected void updateItem(Pair<String,Node> item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            setGraphic(cellBox);
            cellBox.getChildren().clear();
            cellBox.getChildren().addAll(new Text(item.getKey() + " "), item.getValue());
        }
    }
}
