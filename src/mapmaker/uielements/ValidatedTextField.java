/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker.uielements;

import java.util.function.Function;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

/**
 *
 * @author owner
 */
public class ValidatedTextField extends TextField {
    private static PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    private final BooleanProperty invalidProperty = new SimpleBooleanProperty(false){
        @Override
        public void invalidated() {
            pseudoClassStateChanged(INVALID_PSEUDO_CLASS, get());
        }

        @Override
        public Object getBean(){
            return this;
        }

        @Override
        public String getName(){
            return "Invalid Text";
        }
    };
    
    Function<String,Boolean> validateFunction;
    Tooltip tp;
    
    public ValidatedTextField(){
        this("");
    }
    
    public ValidatedTextField(String text){
        this(text, null);
    }
    
    public ValidatedTextField(String text, Function<String,Boolean> validateFunction){
        super(text);
        Tooltip tp = new Tooltip("Invalid Input");
        setValidateFunction(validateFunction);
        textProperty().addListener((o, oV, nV) -> {
            if(this.validateFunction != null)
                setInvalid(this.validateFunction.apply(nV));
        });
        invalidProperty().addListener((o, oV, nV)->{
            if(nV){
                Tooltip.install(this, tp);
            }
            else{
                Tooltip.uninstall(this, tp);
            }
        });
        getStyleClass().add("validated-text-field");
    }
    
    public boolean         isInvalid(){return invalidProperty.get();}
    public void            setInvalid(boolean invalid){invalidProperty.set(invalid);}
    public final BooleanProperty invalidProperty(){return invalidProperty;}
    
    public final String    getTooltipText(){return tooltipTextProperty().get();}
    public final void      setInvalidTooltipText(String text){tooltipTextProperty().set(text);}
    public final StringProperty tooltipTextProperty(){return tp.textProperty();}
    
    public final void      setValidateFunction(Function<String,Boolean> validateFunction){
        this.validateFunction = validateFunction;
    }
}
