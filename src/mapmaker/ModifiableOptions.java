/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.util.Pair;

/**
 *
 * @author owner
 */
public interface ModifiableOptions {
    public ObservableList<Pair<String, Node>> getModifiableOptionList();
}
