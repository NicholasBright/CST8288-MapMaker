/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author owner
 */
public class GuiController {
    static BorderPane rootPane;
    private static MenuBar menuBar;
    
    public static BorderPane createRootPane(){
        rootPane = new BorderPane();
        rootPane.setTop(createMenuBar());
        return rootPane;
    }
    
    private static MenuBar createMenuBar(){
        menuBar = new MenuBar();
        menuBar.setId("MenuBar");
        menuBar.getMenus().addAll(createFileMenu(),createHelpMenu());
        return menuBar;
    }
    
    private static Menu createFileMenu(){
        Menu fileMenu = new Menu("File");
        fileMenu.setId("FileMenu");
        return fileMenu;
    }
    
    private static Menu createHelpMenu(){
        Menu helpMenu = new Menu("Help");
        helpMenu.setId("HelpMenu");
        return helpMenu;
    }
}
