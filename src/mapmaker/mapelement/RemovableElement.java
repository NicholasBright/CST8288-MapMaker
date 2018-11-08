/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.mapelement;

import java.util.List;
import javafx.scene.Node;

/**
 *
 * @author owner
 */
public interface RemovableElement {
    /**
     * 
     * @return A group of things to be removed, this allows you to add other things to the group that need to be removed with this objcet itself
     */
    public List<Node> remove();
}
