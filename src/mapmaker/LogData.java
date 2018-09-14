/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author owner
 */
interface LogData {
    
    abstract void initiateLogging();
    
    static void initiateLogging(String className, Logger logger){
        String CWD = System.getProperty("user.dir");
        try {
            FileHandler fileHandler = new FileHandler(CWD + "/logs/" + className + ".log");
            fileHandler.setFilter((LogRecord lr) -> {
                if(MapMaker.allLogging){
                    return true;
                }
                else if (MapMaker.logging && (lr.getLevel().intValue() >= 900)){
                    return true;
                }
                return false;
            });
            SimpleFormatter sf = new SimpleFormatter();
            fileHandler.setFormatter(sf);
            logger.addHandler(fileHandler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
}
