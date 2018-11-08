package mapmaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import mapmaker.mapelement.ControlPoint;
import mapmaker.mapelement.Path;
import mapmaker.mapelement.Room;

public class ResourceManager{
    private static final ResourceManager RESOURCE_MANAGER = new ResourceManager();
    
    private final String RESOURCE_PATH = "resources/";
    private final String CSS_PATH = RESOURCE_PATH + "css/";
    private final String TXT_PATH = RESOURCE_PATH + "txts/";
    private final String MAPS_PATH = "maps/";
    
    private ResourceManager(){
    }
    
    public String loadTxtToString(String fileName) throws FileNotFoundException{
        String txtString = new String();
        Scanner scanner = new Scanner(new File(TXT_PATH + fileName));
        while(scanner.hasNextLine()){
            txtString += scanner.nextLine() + System.getProperty("line.separator");
        }
        return txtString;
    }
    
    public String getCSSUri(String fileName) throws FileNotFoundException{
        File f = new File(CSS_PATH + fileName);
        if(!f.exists())
            throw new FileNotFoundException("File couldn't be located at " +
                    System.getProperty("user.dir") + "/" + CSS_PATH + fileName);
        return f.toURI().toString();
    }
    
    public void saveMapArea(Stage primary, MapArea ma){
        File mapFolder = new File(MAPS_PATH);
        if(!mapFolder.exists())
            mapFolder.mkdir();
        File file = getFileChooser(primary, true);
        if (file==null)
            return;
        
        StringBuilder sb = new StringBuilder();
        ma.getRooms().forEach((room) -> {
            sb.append("sides ");
            sb.append(room.getNumSides());
            sb.append(System.lineSeparator());
            sb.append("strokeWidth ");
            sb.append(room.getStrokeWidth());
            sb.append(System.lineSeparator());
            sb.append("fill ");
            sb.append(room.getFill().toString().replace("0x", "#").toUpperCase());
            sb.append(" ");
            sb.append(1.0);
            sb.append(System.lineSeparator());
            sb.append("stroke ");
            sb.append(room.getStroke().toString().replace("0x", "#").toUpperCase());
            sb.append(" ");
            sb.append(1.0);
            sb.append(System.lineSeparator());
            sb.append("points ");
            for(ControlPoint cp : room.getControlPointList()){
                sb.append(Math.round(cp.getCenterX()));
                sb.append(" ");
                sb.append(Math.round(cp.getCenterY()));
                sb.append(" ");
            }
            sb.append(System.lineSeparator());
            sb.append("locks ");
            for(Path p : room.getPaths()){
                Room lock = p.getStart();
                if(lock == room)
                    lock = p.getEnd();
                int i=0;
                for(Room r : ma.getRooms()){
                    if(r == lock)
                        break;
                    i++;
                }
                sb.append(i);
                sb.append(" ");
            }
            sb.append(System.lineSeparator());
        });
        
        try{
            if(!file.exists())
                file.createNewFile();
            Files.write( file.toPath(), sb.toString().getBytes());
        }catch( IOException e){
            e.printStackTrace();
        }
    }
    
    public void loadMapArea(Stage primary, MapArea ma){
        //get the file object to load from
        File file = getFileChooser( primary, false);
        if (file==null || !file.exists())
            return;
        try{
            
            //no parallel (threading) here but this is safer
            AtomicInteger index = new AtomicInteger(0);  
            //index.getAndIncrement()/5 means every 5 elements increases by 1
            //allowing for every 5 element placed in the same key
            //for each line in file group every 5 and pass to map area
            Map<Integer, Room> roomMap = new HashMap<>();
            Files.lines(file.toPath()).collect( Collectors.groupingBy( l->index.getAndIncrement()/6)).forEach((i,l)->{
                Room r = new Room();
                for(String s : l){
                   String[] values = s.split(" ");
                   switch(values[0]){
                       case "sides":
                           r.setNumSides(Integer.parseInt(values[1]));
                           break;
                   }
               }
            });
        }catch( IOException e){
            e.printStackTrace();
        }
    }
    
    private File getFileChooser( Stage primary, boolean save){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add( new ExtensionFilter( "Maps", "*.map"));
        fileChooser.setInitialDirectory( Paths.get( MAPS_PATH).toFile());
        return save?fileChooser.showSaveDialog( primary):fileChooser.showOpenDialog( primary);
    }
    
    public static ResourceManager get(){
        return RESOURCE_MANAGER;
    }
}
