package mapmaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import mapmaker.mapelement.ControlPoint;
import mapmaker.mapelement.Path;
import mapmaker.mapelement.Room;

public class ResourceManager{
    private static final Logger LOGGER = Logger.getLogger(ResourceManager.class.getName());
    
    private static final ResourceManager RESOURCE_MANAGER = new ResourceManager();
    
    private final String RESOURCE_PATH = "resources/";
    private final String CSS_PATH = RESOURCE_PATH + "css/";
    private final String TXT_PATH = RESOURCE_PATH + "txts/";
    private final String MAPS_PATH = "maps/";
    
    private ResourceManager(){
        try {
            File logFolder = new File("logs/");
            if(!logFolder.exists())
                logFolder.mkdir();
            FileHandler fh = new FileHandler("logs/" + ResourceManager.class.getSimpleName() + ".txt");
            LOGGER.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException | SecurityException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
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
        int ID = 0;
        for(Room room : ma.getRooms()){
            sb.append("sides ");
            sb.append(room.getNumSides());
            sb.append(System.lineSeparator());
            sb.append("ID ");
            sb.append(ID++);
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
        }
        
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
            Map<Room, String[]> lockMap = new HashMap<>();
            Files.lines(file.toPath()).collect( Collectors.groupingBy( l->index.getAndIncrement()/7)).forEach((i,l)->{
                Room r = new Room();
                for(String s : l){
                    String[] values = s.split(" ");
                    switch(values[0]){
                        case "sides":
                            r.setNumSides(Integer.parseInt(values[1]));
                            break;
                        case "id":
                        case "ID":
                            roomMap.put(Integer.parseInt(values[1]), r);
                            break;
                        case "strokeWidth":
                            r.setStrokeWidth(Double.parseDouble(values[1]));
                            break;
                        case "fill":
                            r.setFill(Paint.valueOf(values[1]));
                            break;
                        case "stroke":
                            r.setFill(Paint.valueOf(values[1]));
                            break;
                        case "locks":
                            lockMap.put(r, values);
                            break;
                        case "points":
                            double cX = 0.0;
                            double cY = 0.0;
                            for(int p=1; p+1< values.length; p+=2){
                                cX += Double.parseDouble(values[p]);
                                cY += Double.parseDouble(values[p+1]);
                            }
                            cX = cX/((values.length-1)/2);
                            cY = cY/((values.length-1)/2);
                            r.normalizeShape(cX, cY, Integer.parseInt(values[1]), Integer.parseInt(values[2]));
                            break;
                        default:
                            LOGGER.log(Level.WARNING, "Unknown line value \"{0}\" in file \"{1}\"", new String[]{values[0], file.toString()});
                            break;
                   }
               }
                ma.add(r);
            });
            lockMap.keySet().forEach((room)->{
                List<Path> paths = room.getPaths();
                String[] locks = lockMap.get(room);
                for(int p=1; p < locks.length; p++){
                    boolean exists = false;
                    for(Path path : paths){
                        if(!exists){
                            if(path.getStart() == room){
                                if(path.getEnd() == roomMap.get(Integer.parseInt(locks[p]))){
                                    exists = true;
                                    break;
                                }
                            }
                            else if(path.getEnd() == room){
                                if(path.getStart() == roomMap.get(Integer.parseInt(locks[p]))){
                                    exists = true;
                                    break;
                                }
                            }
                        }
                    }
                    if(!exists){
                        Room connecting = roomMap.get(Integer.parseInt(locks[p]));
                        Path newPath = new Path(room, connecting);
                        room.addPath(newPath);
                        connecting.addPath(newPath);
                        ma.getChildren().add(newPath);
                        ma.getChildren().add(newPath.getStartPoint());
                        ma.getChildren().add(newPath.getEndPoint());
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
