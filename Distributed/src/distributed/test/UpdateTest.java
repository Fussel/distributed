package distributed.test;


import distributed.update.IDistributedUpdate;
import distributed.update.IncrementalUpdate;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author steffen
 */
public class UpdateTest {
    
    public static void main(String[] args) {
        Layout layout = new PatternLayout("%d{yyyy-MM-dd---HH-mm-ss-SSS} %5p %c: %m%n");
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.DEBUG);
        rootLogger.addAppender(new ConsoleAppender(layout));
        
        IncrementalUpdate.UpdateServer us = new IncrementalUpdate.UpdateServer(4711);
        us.start();
        
        IncrementalUpdate.UpdateClient uc = new IncrementalUpdate.UpdateClient("127.0.0.1", 4711);
        uc.start();
    }
    
}
