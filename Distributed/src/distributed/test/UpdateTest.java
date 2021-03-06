package distributed.test;


import distributed.update.IDistributedUpdate;
import distributed.update.UpdateClient;
import distributed.update.UpdateServer;
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
        Logger rootLogger = Logger.getRootLogger();
        
        UpdateServer us = new UpdateServer(4711);       
        us.start();
        
        UpdateClient uc = new UpdateClient("127.0.0.1", 4711);
        uc.start();
    }
    
}
