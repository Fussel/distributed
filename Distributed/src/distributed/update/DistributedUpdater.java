/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.update;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 *
 * @author steffen
 */
public class DistributedUpdater {
    
    private static final Logger log = Logger.getLogger(DistributedUpdater.class.getName());
    
    public static class UpdateServer extends Thread {
        
       
    }
    
    public static class UpdateClient extends Thread {
        
        public UpdateClient() {
            log.debug("Initialising UpdateClient");
        }
        
        
        
        public void run() {
            
        }
    }
    
}
