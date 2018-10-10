/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.mapelement;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;

/**
 *
 * @author owner
 */
public interface ModifiableProperties {
    public ObservableList<Property> getModifiablePropertiesList();
}
