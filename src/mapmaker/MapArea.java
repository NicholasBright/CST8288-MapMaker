package mapmaker;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mapmaker.mapelement.PolyRoom;
import mapmaker.tool.ToolState;

public class MapArea extends Pane {
    private final ObservableList<PolyRoom> rooms = FXCollections.observableArrayList();
    
    public MapArea(){
        super();
        setId("MapArea");
        
        addEventHandler(MouseEvent.ANY, e -> {
            ToolState.getToolState().getActiveTool().handleMouseEvent(e);
        });
        
        rooms.addListener((ListChangeListener.Change<? extends PolyRoom> c) -> {
            while(c.next()){
                if(c.wasAdded()){
                    c.getAddedSubList()
                        .stream()
                        .forEach((r) -> {
                            if(!super.getChildren().contains(r)){
                                super.getChildren().addAll(r.getControlPoints());
                                super.getChildren().add(r);
                            }
                        });
                }
                else if(c.wasRemoved()){
                    c.getRemoved()
                        .stream()
                        .forEach((r) -> {
                            if(super.getChildren().contains(r)){
                                super.getChildren().removeAll(r.getControlPoints());
                                super.getChildren().remove(r);
                            }
                        });
                }
            }
        });
        
        super.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
            while(c.next()){
                if(c.wasAdded()){
                    c.getAddedSubList()
                        .stream()
                        .forEach((Node n) -> {
                            if(n instanceof PolyRoom)
                                if(!rooms.contains((PolyRoom)n))
                                    add((PolyRoom)n);
                        });
                }
                else if(c.wasRemoved()){
                    c.getRemoved()
                        .stream()
                        .forEach((n) -> {
                            if(n instanceof PolyRoom)
                                if(rooms.contains((PolyRoom)n))
                                    remove((PolyRoom)n);
                        });
                }
            }
        });
    }
    
    public void reset(){
        getChildren().clear();
        rooms.clear();
    }
    
    public void add(PolyRoom r){
        rooms.add(r);
    }
    
    public void remove(PolyRoom r){
        rooms.remove(r);
    }
    
    public void removeAll(List<PolyRoom> l){
        l.stream()
         .forEach( (r) -> {
             remove(r);
         });
    }
    
    public ObservableList<PolyRoom> getRooms(){
        return rooms;
    }
}
