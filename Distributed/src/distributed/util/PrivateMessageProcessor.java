/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.util;

import distributed.dao.PrivateMessage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author steffen
 */
public class PrivateMessageProcessor extends Thread {
    
    private static PrivateMessageProcessor instance;
    private BlockingQueue<PrivateMessage> jobQueue;
    private boolean isRunning;
    
    public static synchronized PrivateMessageProcessor getInstance() {
        if(instance == null) {
            instance = new PrivateMessageProcessor();
            instance.start();
        }
        
        return instance;
    } 
    
    private PrivateMessageProcessor() {
        jobQueue = new LinkedBlockingQueue<>();
        isRunning = true;
    }
    
    
    public void run() {
        PrivateMessage message;
        while(isRunning) {
            try {
                message = jobQueue.take();
                //TODO Do what ever the fuck you want
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void end() {
        isRunning = false;
        //TODO Save to job queue
        instance = null;
    }
    
    public boolean addPrivateMessage(PrivateMessage message) {
        if(!isRunning)
            return false; 
        
        synchronized(jobQueue) {
            jobQueue.add(message);
        }
        
        return true;
    }
}
