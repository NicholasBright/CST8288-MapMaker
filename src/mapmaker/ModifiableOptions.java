/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import javafx.scene.Node;
import javafx.scene.control.ListView;

/**
 *
 * @author owner
 */
public interface ModifiableOptions {
    public void populateListViewWithOptions(ListView<Node> lv);
}
